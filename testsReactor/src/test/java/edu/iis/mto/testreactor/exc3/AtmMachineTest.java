package edu.iis.mto.testreactor.exc3;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class AtmMachineTest {

    private CardProviderService cardProviderService;
    private MoneyDepot moneyDepot;
    private BankService bankService;
    private AtmMachine atmMachine;
    private Card card;

    @Before public void setUp() {
        cardProviderService = Mockito.mock(CardProviderService.class);
        bankService = Mockito.mock(BankService.class);
        moneyDepot = Mockito.mock(MoneyDepot.class);

        atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);
        card = Card.builder().withCardNumber("123456789").withPinNumber(1234).build();
    }

    @Test(expected = WrongMoneyAmountException.class) public void shouldReturnWrongMoneyAmountExceptionIfWrongAmountMoneyGiven() {

        Money money = Money.builder().withAmount(-10).withCurrency(Currency.PL).build();
        atmMachine.withdraw(money, card);
    }

    @Test public void shouldReturnPaymentThreeBanknotes() {

        Money money = Money.builder().withAmount(80).withCurrency(Currency.PL).build();
        Payment result = atmMachine.withdraw(money, card);

        assertThat(result.getValue().size(), Matchers.equalTo(3));

    }

    @Test(expected = AtmException.class) public void shouldThrowCardAuthorizeExceptionOnWrongAuthorization() {

        Money money = Money.builder().withAmount(80).withCurrency(Currency.PL).build();

        try {
            Mockito.when(cardProviderService.authorize(card)).thenThrow(CardAuthorizationException.class);
        } catch (CardAuthorizationException e) {
            e.printStackTrace();
        }
        atmMachine.withdraw(money, card);

    }

    @Test public void shouldReturnMoneyThatUserRequired() {

        Money money = Money.builder().withAmount(120).withCurrency(Currency.PL).build();
        Payment result = atmMachine.withdraw(money, card);

        List<Banknote> listOfBanknotes = result.getValue();
        int amountPaid = 0;
        for (Banknote banknote : listOfBanknotes) {
            amountPaid += banknote.getValue();
        }

        assertThat(amountPaid, Matchers.equalTo(120));

    }

    @Test(expected = WrongMoneyAmountException.class)
    public void shouldThrowWrongMoneyAmountExceptionNoSuchBanknote() {

        Money money = Money.builder().withAmount(8).withCurrency(Currency.PL).build();
        atmMachine.withdraw(money, card);

    }

    @Test public void itCompiles() {
        assertThat(true, equalTo(true));
    }

}
