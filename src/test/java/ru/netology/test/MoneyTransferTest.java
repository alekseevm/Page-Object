package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import ru.netology.page.MoneyTransferPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.page;


class MoneyTransferTest {

    private MoneyTransferPage moneyTransferPage = new MoneyTransferPage();

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromSecondCard() {
        int transferAmount = 10_000;
        String stringTransferAmount = String.valueOf(transferAmount);
        var moneyTransferPage = new MoneyTransferPage();
        var dashboardPage = new DashboardPage();
        int originalFirstCardBalance = dashboardPage.getCardBalance("1");
        int originalSecondCardBalance = dashboardPage.getCardBalance("2");
        dashboardPage.getRefillFirstCardButton().click();
        moneyTransferPage.transfer(DataHelper.getCardInfo("2"), stringTransferAmount);
        dashboardPage.getRefillSecondCardButton().click();
        moneyTransferPage.transfer(DataHelper.getCardInfo("1"), stringTransferAmount);
        int actualFirstCardBalance = dashboardPage.getCardBalance("1");
        int actualSecondCardBalance = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(originalFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(originalSecondCardBalance, actualSecondCardBalance);
    }

    @Test
    void shouldTransferMoneyFromFirstCard() {
        int transferAmount = 10_000;
        String stringTransferAmount = String.valueOf(transferAmount);
        var moneyTransferPage = new MoneyTransferPage();
        var dashboardPage = new DashboardPage();
        int originalFirstCardBalance = dashboardPage.getCardBalance("1");
        int originalSecondCardBalance = dashboardPage.getCardBalance("2");
        dashboardPage.getRefillSecondCardButton().click();
        moneyTransferPage.transfer(DataHelper.getCardInfo("1"), stringTransferAmount);
        dashboardPage.getRefillFirstCardButton().click();
        moneyTransferPage.transfer(DataHelper.getCardInfo("2"), stringTransferAmount);
        int actualFirstCardBalance = dashboardPage.getCardBalance("1");
        int actualSecondCardBalance = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(originalFirstCardBalance, actualFirstCardBalance);
        Assertions.assertEquals(originalSecondCardBalance, actualSecondCardBalance);
    }
}
