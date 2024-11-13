package resource.artifact.domains;

public class DataBaseConnectInfo {
    private String host,password,dataBaseName;
    public DataBaseConnectInfo(String host, String password, String dataBaseName) {
        this.host = host;
        this.password = password;
        this.dataBaseName = dataBaseName;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }
}
