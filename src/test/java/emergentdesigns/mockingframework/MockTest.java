package emergentdesigns.mockingframework;
import java.lang.reflect.InvocationHandler;

import org.junit.Before;
import org.junit.Test;

public class MockTest {
	private Mock mock;
	private CustomerRepository customerRepository;
	private SignUpService signUpService;

	@Before
	public void setup() {
		// Arrange
		mock = new Mock();
		customerRepository = Mock.mock(CustomerRepository.class, mock);
		signUpService = new SignUpService(customerRepository);
	}

	@Test
	public void signupStoresNewCustomer2() throws Exception {
		Customer customer = signUpService.signUp("David", "d@gmail.com");
		verify(customerRepository,mock).insertCustomer("David", "d@gmail.com");
	}
	
	@Test(expected=CustomerExistsException.class)
	public void signupFailsWhenUserExists() throws Exception {
		mock.whenCalledThenReturn("findCustomer", new Customer("David", "d@gmail.com"));
		signUpService.signUp("David", "d@gmail.com");
	}
	
	@Test(expected=CustomerExistsException.class)
	public void signupFailsWhenUserExistsNEWAPROACH() throws Exception {
		
		// hasmap with all the functions called with arguments, ==> return values
		when(customerRepository.findCustomer("d@gmail.com"),mock).thenReturn(new Customer("David", "d@gmail.com"));
		signUpService.signUp("David", "d@gmail.com");
	}
	
	private Stub when(Customer findCustomer, Mock mock) {
		return new Stub(mock);
	}

	@Test(expected=UserNameEmptyException.class)
	public void signupFailsWhenEmailIsEmpty() throws Exception {
		mock.whenCalledThenThrowException("insertCustomer", new AttributeException("email"));
		signUpService.signUp("", "");
	}
	
	private <T> T verify(T objectUnderTest, Mock mock) {
		InvocationHandler handler = new Verification(mock);
		return (T) Mock.proxy(objectUnderTest.getClass().getInterfaces()[0], handler);
	}
}
