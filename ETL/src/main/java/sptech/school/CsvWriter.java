package sptech.school;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

public class CsvWriter {

    public ByteArrayOutputStream writeCsv(List<Metrica> metricas) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        CSVFormat format = CSVFormat.DEFAULT
                .withHeader("cpuPercent", "cpuFreq", "ramPercent", "ramUsed", "diskPercent", "diskUsed",  "dateTime")
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);

        CSVPrinter csvPrinter = new CSVPrinter(writer, format);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (Metrica metrica : metricas) {
            String dataFormatada = metrica.getDateTime() != null ? sdf.format(metrica.getDateTime()) : "NA";
            csvPrinter.printRecord(
                    metrica.getCpuPercent() != null ? metrica.getCpuPercent() : "NA",
                    metrica.getCpuFreq() != null ? metrica.getCpuFreq() : "NA",
                    metrica.getRamPercent() != null ? metrica.getRamPercent() : "NA",
                    metrica.getRamUsed() != null ? metrica.getRamUsed() : "NA",
                    metrica.getDiskPercent() != null ? metrica.getDiskPercent() : "NA",
                    metrica.getDiskUsage() != null ? metrica.getDiskUsage() : "NA",
                    dataFormatada
            );
        }

        csvPrinter.flush();
        writer.close();

        return outputStream;
    }
}
