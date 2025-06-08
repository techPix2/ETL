package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class Main implements RequestHandler<S3Event, String> {

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
    private static final String DESTINATION_BUCKET = "trusted-techpix";

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        try {
            InputStream s3InputStream = s3Client.getObject(sourceBucket, sourceKey).getObjectContent();

            CsvMapper csvMapper = new CsvMapper();
            List<Metrica> metricas = csvMapper.map(s3InputStream);

            CsvWriter csvWriter = new CsvWriter();
            ByteArrayOutputStream csvOutputStream = csvWriter.writeCsv(metricas);

            byte[] csvBytes = csvOutputStream.toByteArray();
            InputStream csvInputStream = new ByteArrayInputStream(csvBytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(csvBytes.length);
            metadata.setContentType("text/csv");

            String destinationKey = sourceKey.replace(".csv", "-processed.csv");
            s3Client.putObject(DESTINATION_BUCKET, destinationKey, csvInputStream, metadata);

            return "Processamento concluído com sucesso. Discos processados: " + csvMapper.getDiscosProcessados();
        } catch (Exception e) {
            context.getLogger().log("Erro: " + e.getMessage());
            return "Erro no processamento: " + e.getMessage();
        }
    }
}
