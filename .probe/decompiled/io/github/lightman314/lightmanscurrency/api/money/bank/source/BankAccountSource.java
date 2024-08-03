package io.github.lightman314.lightmanscurrency.api.money.bank.source;

import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

public abstract class BankAccountSource {

    @Nonnull
    public abstract List<BankReference> CollectAllReferences(boolean var1);

    public List<IBankAccount> CollectAllBankAccounts(boolean isClient) {
        List<IBankAccount> list = new ArrayList();
        for (BankReference br : this.CollectAllReferences(isClient)) {
            IBankAccount ba = br.get();
            if (ba != null) {
                list.add(ba);
            }
        }
        return list;
    }
}