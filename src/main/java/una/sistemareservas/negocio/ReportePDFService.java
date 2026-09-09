package una.sistemareservas.negocio;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import una.sistemareservas.modelo.Recurso;
import una.sistemareservas.modelo.Reserva;

import java.io.File;
import java.time.format.DateTimeFormatter;

public class ReportePDFService {

    public static File generarComprobanteReserva(Reserva reserva) throws Exception {
        String nombreArchivo = "Comprobante_Reserva_" + reserva.getId() + ".pdf";
        File archivoPdf = new File(nombreArchivo);

        PdfWriter writer = new PdfWriter(archivoPdf);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Encabezado
        Paragraph titulo = new Paragraph("SISTEMA DE RESERVA DE RECURSOS")
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(41, 128, 185));

        Paragraph subtitulo = new Paragraph("Comprobante Oficial de Reserva")
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);

        document.add(titulo);
        document.add(subtitulo);

        // Tabla con detalles de la reserva
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        tabla.setWidth(UnitValue.createPercentValue(100));

        DateTimeFormatter fFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter fHora = DateTimeFormatter.ofPattern("HH:mm");

        tabla.addCell(crearCeldaNegrita("Código de Reserva:"));
        tabla.addCell(reserva.getId());

        tabla.addCell(crearCeldaNegrita("Actividad:"));
        tabla.addCell(reserva.getActividad());

        tabla.addCell(crearCeldaNegrita("Fecha:"));
        tabla.addCell(reserva.getFecha() != null ? reserva.getFecha().format(fFecha) : "N/A");

        tabla.addCell(crearCeldaNegrita("Horario:"));
        String horario = (reserva.getHoraInicio() != null ? reserva.getHoraInicio().format(fHora) : "") +
                " - " +
                (reserva.getHoraFin() != null ? reserva.getHoraFin().format(fHora) : "");
        tabla.addCell(horario);

        tabla.addCell(crearCeldaNegrita("Estado:"));
        tabla.addCell(reserva.getEstado() != null ? reserva.getEstado().toString() : "ACTIVA");

        document.add(tabla);

        // Sección de Recursos Asignados
        Paragraph tituloRecursos = new Paragraph("Recursos Asignados")
                .setFontSize(12)
                .setMarginTop(20)
                .setMarginBottom(10);
        document.add(tituloRecursos);

        Table tablaRecursos = new Table(UnitValue.createPercentArray(new float[]{25, 40, 35}));
        tablaRecursos.setWidth(UnitValue.createPercentValue(100));

        tablaRecursos.addHeaderCell(crearCeldaNegrita("Código"));
        tablaRecursos.addHeaderCell(crearCeldaNegrita("Descripción"));
        tablaRecursos.addHeaderCell(crearCeldaNegrita("Categoría"));

        if (reserva.getRecursos() != null && !reserva.getRecursos().isEmpty()) {
            for (Recurso rec : reserva.getRecursos()) {
                tablaRecursos.addCell(rec.getId());
                tablaRecursos.addCell(rec.getDescripcion());
                tablaRecursos.addCell(rec.getCategoria() != null ? rec.getCategoria().getDescripcion() : "N/A");
            }
        } else if (reserva.getRecursoIds() != null) {
            for (String recId : reserva.getRecursoIds()) {
                tablaRecursos.addCell(recId);
                tablaRecursos.addCell("Recurso Asignado");
                tablaRecursos.addCell("N/A");
            }
        }

        document.add(tablaRecursos);

        // Pie de página
        Paragraph pie = new Paragraph("\n\n Documento generado automáticamente por el Sistema de Reserva de Recursos.")
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(new DeviceRgb(120, 120, 120));
        document.add(pie);

        document.close();
        return archivoPdf;
    }

    // Método auxiliar para construir celdas en negrita de forma limpia
    private static Cell crearCeldaNegrita(String texto) {
        Cell cell = new Cell();
        Paragraph p = new Paragraph(texto);
        cell.add(p);
        return cell;
    }
}