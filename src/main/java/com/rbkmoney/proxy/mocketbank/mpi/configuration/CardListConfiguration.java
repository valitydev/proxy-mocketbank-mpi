package com.rbkmoney.proxy.mocketbank.mpi.configuration;

import com.rbkmoney.proxy.mocketbank.mpi.model.Card;
import com.rbkmoney.proxy.mocketbank.mpi.reader.CardReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

@Configuration
public class CardListConfiguration {

    @Value("${fixture.cards}")
    private Resource fixtureCards;

    @Bean
    public List<Card> cardList(CardReader reader) throws IOException {
        return reader.readList(fixtureCards.getInputStream());
    }

}
