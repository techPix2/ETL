package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class CsvMapper {
    private static final Pattern DISCO_PATTERN = Pattern.compile("disco_(\\w)_(used|percent)");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private Set<String> discosProcessados = new TreeSet<>();

    public List<Metrica> map(InputStream inputStream) throws IOException, ParseException {
        List<Metrica> metricas = new ArrayList<>();

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            Set<String> discosIdentificados = identificarDiscos(csvParser.getHeaderMap().keySet());
            this.discosProcessados = discosIdentificados;

            for (CSVRecord record : csvParser) {
                Metrica metrica = new Metrica();

                metrica.setDateTime(DATE_FORMAT.parse(record.get("data_hora")));
                metrica.setCpuFreq(parseDouble(record.get("cpu_freq")));
                metrica.setCpuPercent(parseDouble(record.get("cpu_percent")));
                metrica.setRamUsed(parseDouble(record.get("ram_used")));
                metrica.setRamPercent(parseDouble(record.get("ram_percent")));
                metrica.setSendPackages(parseDouble(record.get("sendPackages")));
                metrica.setReceivePackages(parseDouble(record.get("receivePackages")));

                for (String disco : discosIdentificados) {
                    Double used = parseDouble(record.get("disco_" + disco.toLowerCase() + "_used"));
                    Double percent = parseDouble(record.get("disco_" + disco.toLowerCase() + "_percent"));
                    metrica.addDiskInfo(disco, used, percent);
                }

                metricas.add(metrica);
            }
        }

        return metricas;
    }

    public Set<String> getDiscosProcessados() {
        return Collections.unmodifiableSet(discosProcessados);
    }

    private Set<String> identificarDiscos(Set<String> headers) {
        Set<String> discos = new TreeSet<>();
        for (String header : headers) {
            var matcher = DISCO_PATTERN.matcher(header.toLowerCase());
            if (matcher.matches()) {
                discos.add(matcher.group(1).toUpperCase());
            }
        }
        return discos;
    }

    private Double parseDouble(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NA")) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
