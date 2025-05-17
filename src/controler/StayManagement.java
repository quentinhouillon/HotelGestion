package src.controler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import src.model.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class StayManagement {
    List<Stay> stays = new ArrayList<>();
    ReservationManagement reservations = new ReservationManagement();
    private Database database = new Database();

    public StayManagement() {
        this.getAll();
    }

    public Stay[] getAll() {
        stays.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Stay"));
        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            int reservationID = (int) row.get("reservation_id");
            String payment = (String) row.get("payment");

            if (reservations.getReservationByID(reservationID) != null)
                stays.add(new Stay(id, reservations.getReservationByID(reservationID)));
        }
        return stays.toArray(Stay[]::new);
    }

    public Stay getStayByResercation(Reservation reservation) {
        for (Stay stay : this.getAll()) {
            if (stay.getReservation().getID() == reservation.getID()) {
                return stay;
            }
        }
        return null;
    }

    public void add(Reservation reservation) {
        List<Map<String, Object>> existingRows = database.executeReadQuery(
                "SELECT * FROM Stay WHERE reservation_id = " + reservation.getID());
        if (existingRows.isEmpty()) {
            database.executeUpdateQuery(
                    "INSERT INTO Stay (reservation_id, payment) VALUES (?, ?)",
                    new Object[] { reservation.getID(), null });
        }

        List<Map<String, Object>> rows = database
                .executeReadQuery("SELECT * FROM Stay ORDER BY id DESC LIMIT 1");
        int id = -1;
        if (!rows.isEmpty()) {
            id = (int) rows.get(0).get("id");
        }
        stays.add(new Stay(id, reservation));
    }

    public void remove(Stay stay) {
        database.executeUpdateQuery("DELETE FROM Consommation WHERE Stay_id = ?", new Object[] { stay.getID() });
        database.executeUpdateQuery("DELETE FROM Stay WHERE id = ?", new Object[] { stay.getID() });
        stays.remove(stay);
    }

    public void clear() {
        stays.clear();
    }

    public boolean contains(Reservation reservation) {
        for (Stay stay : stays) {
            if (stay.getReservation() == reservation) {
                return true;
            }
        }
        return false;
    }

    public boolean stayStarted(Reservation reservation) {
        if ((reservation.getDuration()[0].isBefore(java.time.LocalDate.now()) ||
                reservation.getDuration()[0].isEqual(java.time.LocalDate.now())) &&
                stays.stream().noneMatch(stay -> stay.getReservation().equals(reservation))) {
            return true;
        }
        return false;
    }

    public void generatePDF(Stay stay, String filePath) {
        Document document = new Document();
        java.io.File directory = new java.io.File("./factures");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Les détails de votre séjour", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Paragraph stayInfo = new Paragraph(
                    String.format(
                            "Nom : %s\nPrénom : %s\nDate d'arrivée : %s\nDate de départ : %s\nDurée du séjour : %d nuit(s)",
                            stay.getReservation().getClient().getLastName(),
                            stay.getReservation().getClient().getName(),
                            stay.getReservation().getDuration()[0].toString(),
                            stay.getReservation().getDuration()[1].toString(),
                            java.time.temporal.ChronoUnit.DAYS.between(
                                    stay.getReservation().getDuration()[0],
                                    stay.getReservation().getDuration()[1])),
                    infoFont);
            stayInfo.setSpacingAfter(20);
            document.add(stayInfo);

            PdfPTable table = new PdfPTable(2); // 2 colonnes
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);
            table.setWidths(new float[] { 3, 1 }); // Largeur des colonnes

            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Prestations", headerFont));
            header1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell header2 = new PdfPCell(new Phrase("Montant", headerFont));
            header1.setBackgroundColor(BaseColor.BLACK);
            header2.setBackgroundColor(BaseColor.BLACK);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.setPadding(8);
            header2.setPadding(8);
            table.addCell(header1);
            table.addCell(header2);

            Font cellFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            double roomPricePerNight = stay.getReservation().getRoom().getPrice();
            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                    stay.getReservation().getDuration()[0],
                    stay.getReservation().getDuration()[1]);
            double totalRoomPrice = roomPricePerNight * nights;

            PdfPCell roomCell = new PdfPCell(new Phrase("Chambre n°" + stay.getReservation().getRoom().getRoomNumber() + " ("
                    + roomPricePerNight + " € x " + nights + " nuit(s))", cellFont));
            roomCell.setPadding(8);
            table.addCell(roomCell);

            PdfPCell roomPriceCell = new PdfPCell(new Phrase(String.format("%.2f €", totalRoomPrice), cellFont));
            roomPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            roomPriceCell.setPadding(8);
            table.addCell(roomPriceCell);

            for (int i = 0; i < stay.getConso().length; i++) {
                PdfPCell consoCell = new PdfPCell(new Phrase(stay.getConso()[i], cellFont));
                consoCell.setPadding(8);
                table.addCell(consoCell);

                PdfPCell consoPriceCell = new PdfPCell(
                        new Phrase(String.format("%.2f €", stay.getPrice()[i]), cellFont));
                consoPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                consoPriceCell.setPadding(8);
                table.addCell(consoPriceCell);
            }

            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            PdfPCell totalLabel = new PdfPCell(new Phrase("Total du séjour", totalFont));
            totalLabel.setPadding(8);
            totalLabel.setHorizontalAlignment(Element.ALIGN_LEFT); // Aligné avec "Prestations"
            totalLabel.setBackgroundColor(BaseColor.BLACK); // Fond noir
            table.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(new Phrase(String.format("%.2f €", stay.getTotalPrice()), totalFont));
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT); // Aligné avec "Montant"
            totalValue.setPadding(8);
            totalValue.setBackgroundColor(BaseColor.BLACK); // Fond noir
            table.addCell(totalValue);

            document.add(table);

            Font footerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.ITALIC);
            Paragraph footer = new Paragraph(
                    "Le Continental vous remercie de votre visite\net espère que vous avez passé un agréable séjour.",
                    footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

        } catch (DocumentException | java.io.IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        try {
            java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}