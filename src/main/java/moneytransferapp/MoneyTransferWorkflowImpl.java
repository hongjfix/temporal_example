package moneytransferapp;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.workflow.Workflow;
import io.temporal.common.RetryOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

// @@@SNIPSTART money-transfer-project-template-java-workflow-implementation
public class MoneyTransferWorkflowImpl implements MoneyTransferWorkflow {
    private static final String WITHDRAW = "Withdraw";
    // RetryOptions specify how to automatically handle retries when Activities fail.
    private final RetryOptions retryoptions = RetryOptions.newBuilder()
            .setInitialInterval(Duration.ofSeconds(1))
            .setMaximumInterval(Duration.ofSeconds(100))
            .setBackoffCoefficient(2)
            .setMaximumAttempts(500)
            .build();
    private final ActivityOptions defaultActivityOptions = ActivityOptions.newBuilder()
            // Timeout options specify when to automatically timeout Activities if the process is taking too long.
            .setStartToCloseTimeout(Duration.ofSeconds(5))
            // Optionally provide customized RetryOptions.
            // Temporal retries failures by default, this is simply an example.
            .setRetryOptions(retryoptions)
            .build();
    // ActivityStubs enable calls to methods as if the Activity object is local, but actually perform an RPC.
    private final Map<String, ActivityOptions> perActivityMethodOptions = new HashMap<String, ActivityOptions>(){{
        put(WITHDRAW, ActivityOptions.newBuilder().setHeartbeatTimeout(Duration.ofSeconds(5)).build());
    }};
    private final AccountActivity account = Workflow.newActivityStub(AccountActivity.class, defaultActivityOptions, perActivityMethodOptions);

    // The transfer method is the entry point to the Workflow.
    // Activity method executions can be orchestrated here or from within other Activity methods.
    @Override
    public void transfer(String fromAccountId, String toAccountId, String referenceId, double amount) {
        try {
            for(int i=0; i < 100; i++){
                //if amount is 0, should throw an exception
                if(amount == 0){
                    System.out.println("amount = 0 ");
                    throw new RuntimeException("amount is 0");
                }
                if(i%2 == 0) {
                    account.payment(fromAccountId, String.valueOf(i), amount);
                }else {
                    account.notify(toAccountId, String.valueOf(i), amount);
                }
                System.out.println("loop:"+i);
            }



        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
// @@@SNIPEND
