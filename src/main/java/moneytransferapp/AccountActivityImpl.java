package moneytransferapp;

// @@@SNIPSTART money-transfer-project-template-java-activity-implementation
public class AccountActivityImpl implements AccountActivity {

    @Override
    public void payment(String accountId, String referenceId, double amount) {

        System.out.printf(
                "\npayment $%f from account %s. ReferenceId: %s\n",
                amount, accountId, referenceId
        );
    }

    @Override
    public void notify(String accountId, String referenceId, double amount) {

        System.out.printf(
                "\nnotify $%f into account %s. ReferenceId: %s\n",
                amount, accountId, referenceId
        );
        // Uncomment the following line to simulate an Activity error.
        // throw new RuntimeException("simulated");
    }
}
// @@@SNIPEND
