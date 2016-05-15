package emergentdesigns.mockingframework;

public class MethodNotCalledError extends AssertionError {

	public MethodNotCalledError(String methodName) {
		super("Method '" + methodName + "' not called");
	}

}
