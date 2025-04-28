package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class CsvWriter {

    public ByteArrayOutputStream writeCsv(List<Metrica> metricas) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        Set<String> todosDiscos = new TreeSet<>();
        metricas.stream()
                .flatMap(m -> m.getDiscos().keySet().stream())
                .forEach(todosDiscos::add);

        List<String> headers = new ArrayList<>(Arrays.asList(
                "data_hora", "cpu_freq", "cpu_percent", "ram_used", "ram_percent"));

        for (String disco : todosDiscos) {
            headers.add("disco_" + disco + "_used");
            headers.add("disco_" + disco + "_percent");
        }

        CSVFormat format = CSVFormat.DEFAULT
                .withHeader(headers.toArray(new String[0]))
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL);

        CSVPrinter csvPrinter = new CSVPrinter(writer, format);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Metrica metrica : metricas) {

            List<Object> record = new ArrayList<>(Arrays.asList(
                    sdf.format(metrica.getDateTime()),
                    metrica.getCpuFreq() != null ? metrica.getCpuFreq() : "NA",
                    metrica.getCpuPercent() != null ? metrica.getCpuPercent() : "NA",
                    metrica.getRamUsed() != null ? metrica.getRamUsed() : "NA",
                    metrica.getRamPercent() != null ? metrica.getRamPercent() : "NA"
            ));

            for (String disco : todosDiscos) {
                Metrica.DiskInfo info = metrica.getDiscos().get(disco);
                if (info != null) {
                    record.add(info.getUsed() != null ? info.getUsed() : "NA");
                    record.add(info.getPercent() != null ? info.getPercent() : "NA");
                } else {
                    record.add("NA");
                    record.add("NA");
                }
            }

            csvPrinter.printRecord(record);
        }

        csvPrinter.flush();
        writer.close();

        return outputStream;
    }
}