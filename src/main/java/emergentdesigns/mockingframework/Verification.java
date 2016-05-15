package emergentdesigns.mockingframework;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Verification implements InvocationHandler {

	private Mock mock;

	public Verification(Mock mock) {
		this.mock = mock;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		mock.verifyCalledWith(methodName, args);
		return null;
	}

}
