package pages.appiumDemo;

public enum HomePageLinks {
    EnterSomeValues("EnterValue")
    ,ContactUs("ContactUs")
    ,ScrollView("ScrollView")
    ,TabView("TabView")
    ,LongClick("LongClick")
    ,Login("Login")
    ,Time("Time")
    ,Date("Date")
    ,Hybrid("hybrid")
    ,Pinch("pinch")
    ,Drag("drag")
    ,Crash("crash")
    ,AutoComplete("autocomlete");

    private final String method;
    public String getMethod() {
        return method;
    }
    HomePageLinks(String method) {
        this.method = method;
    }}
