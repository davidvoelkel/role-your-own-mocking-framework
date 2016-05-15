package emergentdesigns.mockingframework;

public interface CustomerRepository {

	void insertCustomer(String name, String email);

	Customer findCustomer(String email);

}