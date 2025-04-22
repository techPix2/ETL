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
            String dataFormatada = sdf.format(metrica.getDateTime());
            csvPrinter.printRecord(
                    metrica.getCpuPercent(),
                    metrica.getCpuFreq(),
                    metrica.getRamPercent(),
                    metrica.getRamUsed(),
                    metrica.getDiskPercent(),
                    metrica.getDiskUsage(),
                    dataFormatada
            );
        }

        csvPrinter.flush();
        writer.close();

        return outputStream;
    }
}