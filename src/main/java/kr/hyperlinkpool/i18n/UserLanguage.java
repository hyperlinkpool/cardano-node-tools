package kr.hyperlinkpool.i18n;

public enum UserLanguage {

	KR("ko")
	,US("en")
	,JP("ja")
	;
	
	private String userLanguage;
	
	UserLanguage(String userLanguage){
		this.setUserLanguage(userLanguage);
	}
	
	public String getUserLanguage() {
		return userLanguage;
	}
	
	public void setUserLanguage(String userLanguage) {
		this.userLanguage = userLanguage;
	}

	public static UserLanguage getUserLanguage(String key) {
		UserLanguage[] values = UserLanguage.values();
		for(UserLanguage userLanguage : values) {
			if(key.equals(userLanguage.getUserLanguage())) {
				return userLanguage;
			}
		}
		return null;
	}

}
