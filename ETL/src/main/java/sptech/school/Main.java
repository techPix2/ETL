package sptech.school;

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

    // Criação do cliente S3 para acessar os buckets
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

    // Bucket de destino para o CSV gerado
    private static final String DESTINATION_BUCKET = "myawsbucketcuratedsptech";

    @Override
    public String handleRequest(S3Event s3Event, Context context) {

        // Extraímos o nome do bucket de origem e a chave do arquivo JSON
        String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        try {
            // Leitura do arquivo JSON do bucket de origem
            InputStream s3InputStream = s3Client.getObject(sourceBucket, sourceKey).getObjectContent();

            // Conversão do JSON para uma lista de objetos sptech.school.Stock usando o sptech.school.sptech.school.Mapper
            Mapper mapper = new Mapper();
            List<Metrica> metricas = mapper.map(s3InputStream);

            // Geração do arquivo CSV a partir da lista de sptech.school.Stock usando o sptech.school.sptech.school.CsvWriter
            CsvWriter csvWriter = new CsvWriter();
            ByteArrayOutputStream csvOutputStream = csvWriter.writeCsv(metricas);

            // Converte o ByteArrayOutputStream para InputStream para enviar ao bucket de destino
            byte[] csvBytes = csvOutputStream.toByteArray();
            InputStream csvInputStream = new ByteArrayInputStream(csvBytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(csvBytes.length);
            metadata.setContentType("text/csv");

            s3Client.putObject(DESTINATION_BUCKET, sourceKey.replace(".json", ".csv"), csvInputStream, metadata);

            return "Sucesso no processamento";
        } catch (Exception e) {
            // Tratamento de erros e log do contexto em caso de exceção
            context.getLogger().log("Erro: " + e.getMessage());
            return "Erro no processamento";
        }
    }
}