package edu.iis.mto.testreactor.exc3;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class AtmMachineTest {

    private CardProviderService cardProviderService;
    private MoneyDepot moneyDepot;
    private BankService bankService;


    @Before
    public void setUp() {
        cardProviderService = Mockito.mock(CardProviderService.class);
        bankService = Mockito.mock(BankService.class);
        moneyDepot = Mockito.mock(MoneyDepot.class);
    }


    @Test(expected = WrongMoneyAmountException.class)
    public void shouldReturnWrongMoneyAmountExceptionIfWrongAmountMoneyGiven(){

        AtmMachine atmMachine = new AtmMachine(cardProviderService, bankService, moneyDepot);
        Money money = Money.builder().withAmount(-10).withCurrency(Currency.PL).build();
        Card card = Card.builder().withCardNumber("123456789").withPinNumber(1234).build();
        atmMachine.withdraw(money, card);
    }

    @Test
    public void itCompiles() {
        assertThat(true, equalTo(true));
    }

}
