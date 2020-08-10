package com.rbkmoney.proxy.mocketbank.mpi.reader;

import com.rbkmoney.proxy.mocketbank.mpi.model.Card;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class CardReader implements BeanReader<Card> {

    private static final String REGEXP = ", ";

    @Override
    public List<Card> readList(InputStream is) {
        return extractListFromFile(is,
                line -> {
                    String[] p = line.split(REGEXP);
                    return new Card(p[0], p[1], p[2]);
                });
    }
}
