package com.scloud;

import com.scloud.catalog.model.ProductData;
import com.scloud.catalog.service.ProductDataService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * Listener which cares about loading all the data on startup. <br>
 * Runs every time when application is started, hence relies on the
 * proper cleanup of the data from the storage mechanism side.
 */
@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final String ZERO = "0";

    private final ProductDataService productDataService;

    @Autowired
    public StartupApplicationListener(ProductDataService productDataService) {
        this.productDataService = productDataService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Resource resource = new ClassPathResource("/jcpenney_com-ecommerce_sample.csv");
        try (Reader reader = new FileReader(resource.getFile());
             CSVParser csvRecords = new CSVParser(reader, CSVFormat.EXCEL.withHeader())) {

            Collection<ProductData> productData = mapToProducts(csvRecords);
            productDataService.saveAll(productData);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Collection<ProductData> mapToProducts(Iterable<CSVRecord> csvRecords) {
        Collection<ProductData> productData = new ArrayList<>();

        for (CSVRecord csvRecord : csvRecords) {
            productData.add(new ProductData.Builder(csvRecord.get("uniq_id"), csvRecord.get("sku"))
                    .title(csvRecord.get("name_title"))
                    .description(csvRecord.get("description"))
                    .prices(csvRecord.get("list_price"), csvRecord.get("sale_price"))
                    .category(csvRecord.get("category"), csvRecord.get("category_tree"))
                    .averageRating(csvRecord.get("average_product_rating"))
                    .urls(csvRecord.get("product_url"), csvRecord.get("product_image_urls"))
                    .brand(csvRecord.get("brand"))
                    .reviews(csvRecord.get("Reviews"), defaultIfBlank(csvRecord.get("total_number_reviews"), ZERO))
                    .build());
        }

        return productData;
    }

}
