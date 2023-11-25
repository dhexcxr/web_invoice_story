package objects;

public class LoginResponse {
	
	private Result result = Result.FAILED;
	
	boolean matchingHash = false;
	Employee loginEmployee;

	public enum Result {
		FAILED,
		EMPLOYEE,
		ROOT
	}
	
	public LoginResponse() {
		// TODO Auto-generated constructor stub
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public boolean isMatchingHash() {
		return matchingHash;
	}

	public void setMatchingHash(boolean response) {
		this.matchingHash = response;
	}

	public Employee getLoginEmployee() {
		return loginEmployee;
	}

	public void setLoginEmployee(Employee loginEmployee) {
		this.loginEmployee = loginEmployee;
	}

}
