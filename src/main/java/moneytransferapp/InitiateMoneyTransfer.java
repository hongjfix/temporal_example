package moneytransferapp;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.api.enums.v1.WorkflowExecutionStatus;
import io.temporal.api.workflow.v1.WorkflowExecutionInfo;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionRequest;
import io.temporal.api.workflowservice.v1.DescribeWorkflowExecutionResponse;
import io.temporal.api.workflowservice.v1.WorkflowServiceGrpc;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.UUID;

// @@@SNIPSTART money-transfer-project-template-java-workflow-initiator
public class InitiateMoneyTransfer {

    public static void main(String[] args) throws Exception {

        // WorkflowServiceStubs is a gRPC stubs wrapper that talks to the local Docker instance of the Temporal server.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(Shared.MONEY_TRANSFER_TASK_QUEUE)
                // A WorkflowId prevents this it from having duplicate instances, remove it to duplicate.
                .setWorkflowId("money-transfer-workflow-1")
                .build();
        // WorkflowClient can be used to start, signal, query, cancel, and terminate Workflows.
        WorkflowClient client = WorkflowClient.newInstance(service);
        // WorkflowStubs enable calls to methods as if the Workflow object is local, but actually perform an RPC.
        MoneyTransferWorkflow workflow = client.newWorkflowStub(MoneyTransferWorkflow.class, options);
        String referenceId = UUID.randomUUID().toString();
        String fromAccount = "001-001";
        String toAccount = "002-002";
        double amount = 18.74;
        //double amount = 0;
        // Asynchronous execution. This process will exit after making this call.
        WorkflowExecution we = WorkflowClient.start(workflow::transfer, fromAccount, toAccount, referenceId, amount);

//        System.out.printf("\nwaiting for 10 s.....");
//        Thread.sleep(10000);
//        DescribeWorkflowExecutionRequest describeRequest =
//                DescribeWorkflowExecutionRequest.newBuilder()
//                        .setNamespace("default")
//                        .setExecution(we)
//                        .build();
//        WorkflowServiceGrpc.WorkflowServiceBlockingStub stub = service.blockingStub();
//        DescribeWorkflowExecutionResponse executionDetail =
//                stub.describeWorkflowExecution(describeRequest);
//        WorkflowExecutionInfo instanceMetadata = executionDetail.getWorkflowExecutionInfo();
//        System.out.printf("\ninstanceMetadata.status: %s", instanceMetadata.getStatus());
//
//        //if status is still running, we can do something, include terminate and rollback
//        if(WorkflowExecutionStatus.WORKFLOW_EXECUTION_STATUS_RUNNING == instanceMetadata.getStatus()){
//
//            //terminate
//            WorkflowStub untyped = WorkflowStub.fromTyped(workflow);
//            untyped.terminate("termination by hand");
//
//            //rollback
//            System.out.printf("\nrollback processing....");
//
//            //send a alert
//            System.out.printf("\nalert！");
//
//        }

        System.out.printf("\nTransfer of $%f from account %s to account %s is processing\n", amount, fromAccount, toAccount);
        System.out.printf("\nWorkflowID: %s RunID: %s", we.getWorkflowId(), we.getRunId());
        System.exit(0);
    }
}
// @@@SNIPEND
