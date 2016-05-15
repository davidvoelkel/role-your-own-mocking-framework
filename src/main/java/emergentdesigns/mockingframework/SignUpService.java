package emergentdesigns.mockingframework;

public class SignUpService {

	private CustomerRepository customerRepository;

	public SignUpService(CustomerRepository customerDatabase) {
		this.customerRepository = customerDatabase;
	}

	public Customer signUp(String name, String email) {
		Customer customer = customerRepository.findCustomer(email);
		if (customer != null) {
			throw new CustomerExistsException();
		}
		try {
			customerRepository.insertCustomer(name, email);
		} catch (AttributeException e) {
			throw new UserNameEmptyException(e);
		}
		return null;
	}

	public Customer userExists(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
