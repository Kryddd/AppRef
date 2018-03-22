package services;

public class ServiceBundle {
	private Class<? extends Service> classService;
	private boolean FTPRequired;
	private boolean activated;
	
	/**
	 * Classe dans laquelle est stockée le classes de Service et leurs infos requises
	 * @param classService
	 * @param FTPRequired
	 * @param activated
	 */
	public ServiceBundle(Class<? extends Service> classService, boolean FTPRequired, boolean activated) {
		this.classService = classService;
		this.FTPRequired = FTPRequired;
		this.activated = activated;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public Class<? extends Service> getClassService() {
		return classService;
	}

	public boolean isFTPRequired() {
		return FTPRequired;
	}
}
