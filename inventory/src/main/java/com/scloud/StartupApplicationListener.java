package com.scloud;

import com.scloud.inventory.service.ProductAvailabilityService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.scloud.inventory.model.ProductAvailability;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ProductAvailabilityService service;
    private final Random random;

    @Autowired
    public StartupApplicationListener(ProductAvailabilityService service) {
        this.service = service;
        this.random = new Random();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Resource resource = new ClassPathResource("/jcpenney_com-ecommerce_id.csv");

        try (Reader reader = new FileReader(resource.getFile());
             CSVParser csvRecords = new CSVParser(reader, CSVFormat.EXCEL.withHeader())) {

            var products = mapToProductAvailability(csvRecords);
            service.saveAll(products);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Collection<ProductAvailability> mapToProductAvailability(CSVParser csvRecords) throws IOException {
        return csvRecords.getRecords().stream()
                .map(csvRecord -> new ProductAvailability(csvRecord.get("uniq_id"), random.nextBoolean()))
                .collect(Collectors.toList());
    }

}
