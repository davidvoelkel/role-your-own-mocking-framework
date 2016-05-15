package emergentdesigns.mockingframework;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import junit.framework.ComparisonFailure;

public class Mock implements InvocationHandler {

	private Map<String, Object[]> spiesForMethod = new HashMap<>();
	private Object returnValue;
	private Map<String, Exception> exceptionsToThrow = new HashMap<>();

	public boolean wasCalled(String method) {
		return spiesForMethod.containsKey(method);
	}

	public void verifyCalledWith(String methodName, Object... expectedArgs) {
		if(!wasCalled(methodName)) {
			throw new MethodNotCalledError(methodName);
		}
		Object[] actualArgs = spiesForMethod.get(methodName);
		if (areTheyTheSame(actualArgs, expectedArgs)) {
			throw methodArgsMismatch(methodName, actualArgs, expectedArgs);
		}
	}

	private ComparisonFailure methodArgsMismatch(String methodName, Object[] actualArgs, Object... expectedArgs) {
		return new ComparisonFailure("Method '" + methodName + "' called with the wrong arguments", 
				renderArray(actualArgs), renderArray(expectedArgs));
	}

	private boolean areTheyTheSame(Object[] actualArgs, Object... expectedArgs) {
		boolean sameLength = actualArgs.length != expectedArgs.length;
		boolean allElementsAreSame = IntStream.range(0, expectedArgs.length)
					    					  .allMatch(i -> actualArgs[i].equals(expectedArgs[i]));
		return sameLength || !allElementsAreSame;
	}

	private String renderArray(Object... args) {
		return Arrays.asList(args).toString();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		spiesForMethod.put(methodName, args);
		if (exceptionsToThrow.containsKey(methodName)) {
			throw exceptionsToThrow.get(methodName);
		}
		return returnValue;
	}

	public void whenCalledThenReturn(String methodName, Object customer) {
		returnValue = customer;
	}

	public void whenCalledThenThrowException(String methodName, Exception exception) {
		exceptionsToThrow.put(methodName, exception);
	}

	static <T> T mock(Class<T> class1, Mock mock2) {
		return proxy(class1, mock2);
	}

	public static <T> T proxy(Class<T> class1, InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(
				class1.getClassLoader(),
				new Class[] { class1 },
				handler);
	}

}
