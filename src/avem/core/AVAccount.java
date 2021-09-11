package avem.core;

public class AVAccount {


    public static int ADMIN = 0;
    public static int INTERNAL_STAFF = 1;
    public static int EXTERNAL_STAFF = 2;
    public static int STUDENT_GUEST = 3;

    private String username;
    private String password;
    private String name;
    private int access;
    private String additionalInfo;


    public AVAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.access = STUDENT_GUEST;
        this.name = "Unknown";
        this.additionalInfo = "Not set.";
    }

    public AVAccount(String username, String password, int access) {
        this.username = username;
        this.password = password;
        this.access = access;
        this.name = "Unknown";
        this.additionalInfo = "Not set.";
    }

    public AVAccount(String username, String password, int access, String name) {
        this.username = username;
        this.password = password;
        this.access = access;
        this.name = name;
        this.additionalInfo = "Not set.";
    }

    public AVAccount(String username, String password, int access, String name,  String additionalInfo) {
        this.username = username;
        this.password = password;
        this.access = access;
        this.name = name;
        this.additionalInfo = additionalInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getFormattedString() {
        return
                username + "\n" +
                password + "\n" +
                access + "\n" +
                name + "\n" +
                additionalInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AVAccount && obj != null) {
            AVAccount avAccount = (AVAccount) obj;
            if (this.username.equals(avAccount.getUsername()) &&
                    this.password.equals(avAccount.getPassword())
            ) {
                return true;
            }
        }
        return false;
    }
}
