package template.engine.comparison;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.HashMap;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;

public class Main {

    public static void $$main(String[] args) {
        // GraphServiceClient.builder().authenticationProvider(new
        // IAuthenticationProvider() {

        // @Override
        // public void authenticateRequest(IHttpRequest request) {
        // try {
        // request.addHeader("Authorization", "Bearer " + "token");
        // // This header has been added to identify this sample in the Microsoft Graph
        // // service.
        // // If you're using this code for your project please remove the following
        // line.
        // // request.addHeader("SampleID", "android-java-connect-sample");
        // // Log.getLog() .i("Connect", "Request: " + request.toString());
        // } catch (ClientException e) {
        // e.printStackTrace();
        // } catch (NullPointerException e) {
        // e.printStackTrace();
        // }
        // }
        // }).buildClient().me().outlook().
    }

    public static void $1main(String[] args) throws IOException {
        HashMap<String, Object> scopes = new HashMap<String, Object>();
        scopes.put("name", "hello world");
        DefaultMustacheFactory factory = new DefaultMustacheFactory();
        Mustache mustache = factory.compile(new StringReader("{{name}}"), "hei");
        mustache.execute(new PrintWriter(System.out), scopes).flush();
    }
}