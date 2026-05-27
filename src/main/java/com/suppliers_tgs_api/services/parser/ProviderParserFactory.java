package com.suppliers_tgs_api.services.parser;
import java.util.List;
import org.springframework.stereotype.Component;
import com.suppliers_tgs_api.model.ProviderName;
import lombok.RequiredArgsConstructor;
import com.suppliers_tgs_api.services.ProviderParser;
import org.springframework.stereotype.Service;

@Service
@Component
@RequiredArgsConstructor
public class ProviderParserFactory {

    private final List<ProviderParser> parsers;

    public ProviderParser getParser(ProviderName name) {
        return parsers.stream()
                .filter(p -> p.supports() == name)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Parser not found"));
    }
}