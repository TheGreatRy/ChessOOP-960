package chess;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class TestProgressListener implements TestExecutionListener {

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (testIdentifier.isTest()) {
            System.out.println("\n   [\u001B[96mRUNNING\u001B[0m] " + testIdentifier.getDisplayName());
        }
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult result) {
        if (testIdentifier.isTest()) {
            String color = (result.getStatus().equals("SUCCESSFUL")) ? "\u001B[91m" : "\u001B[92m";
            System.out.println("   [" + color + result.getStatus() + "\u001B[0m] " + testIdentifier.getDisplayName() + "\n");
        }
    }
}
