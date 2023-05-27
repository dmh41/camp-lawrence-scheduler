import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.*;


public class SheetStart {
  private static Sheets service;
  private static List<List<String>> prefData;
  private static List<List<String>> actData;

  private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
  private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
  private static final String TOKENS_DIRECTORY_PATH = "tokens";

  /**
   * Global instance of the scopes required by this quickstart.
   * If modifying these scopes, dxelete your previously saved tokens/ folder.
   */
  private static final List<String> SCOPES =
      Collections.singletonList(SheetsScopes.SPREADSHEETS);
  private static final String CREDENTIALS_FILE_PATH = "credentials.json";

  /**
   * Creates an authorized Credential object.
   *
   * @param HTTP_TRANSPORT The network HTTP Transport.
   * @return An authorized Credential object.
   * @throws IOException If the credentials.json file cannot be found.
   */
  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
    // Load client secrets.
    InputStream in = SheetStart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
    if (in == null) {
      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
        .setAccessType("offline")
        .build();
    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
  }

  public static List<List<String>> getPref(){
    return prefData;
  }
  public static List<List<String>> getAct(){
    return actData;
  }

  public static void clearData() throws IOException {
    final String spreadsheetId = "1XwtiKc1Ir2ruhvZESVtFh9VzHdYWj0pgzBDt6b43xqk";
    final String range = "Output!A2:H"; // Specify the range of data to be cleared

    service.spreadsheets().values().clear(spreadsheetId, range, new ClearValuesRequest()).execute();
    //System.out.println("Data cleared successfully.");
}

  public static void writeOutput(List<Camper> data) throws GoogleJsonResponseException, IOException{
    List<List<Object>> output = new ArrayList<>();
    //List<Object> row = new ArrayList<>();
    for(Camper c: data){ 
      List<Object> row = new ArrayList<>();
      row.add(c.getFirst());
      row.add(c.getLast());
      row.add(c.getCabin());
      List<String> temp = c.getClasses();
      for(int i = 0; i < temp.size(); i++){
        row.add(temp.get(i));
      }
      output.add(row);
    }

    final String spreadsheetId = "1XwtiKc1Ir2ruhvZESVtFh9VzHdYWj0pgzBDt6b43xqk";
    final String range = "Output!A2:H";
    final String valueInputOption = "RAW";

    UpdateValuesResponse result = null;
    try {
      // Updates the values in the specified range.
      ValueRange body = new ValueRange()
          .setValues(output);
      result = service.spreadsheets().values().update(spreadsheetId, range, body)
          .setValueInputOption(valueInputOption)
          .execute();
      System.out.printf("%d cells updated.", result.getUpdatedCells());
    } catch (GoogleJsonResponseException e) {
      // TODO(developer) - handle error appropriately
      GoogleJsonError error = e.getDetails();
      if (error.getCode() == 404) {
        System.out.printf("Spreadsheet not found with id '%s'.\n", spreadsheetId);
      } else {
        e.printStackTrace();
        throw e;
      }
    }
    //return result;
  }


  /**
   * Prints the names and majors of students in a sample spreadsheet:
   * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
   */
  public static void main(String... args) throws IOException, GeneralSecurityException {
    // Build a new authorized API client service.
    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    final String spreadsheetId = "1XwtiKc1Ir2ruhvZESVtFh9VzHdYWj0pgzBDt6b43xqk";
    service =
        new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();
    
    //Initialize for Preference read
    prefData = new ArrayList<>();
    final String range = "Preferences!A2:M";
    ValueRange response = service.spreadsheets().values().get(spreadsheetId, range).execute();
    List<List<Object>> values = response.getValues();

    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List<Object> row : values) {
        List<String> tempRow = new ArrayList<>();
        for(int i = 0;i<13;i++){
          tempRow.add(row.get(i).toString());
        }
        prefData.add(tempRow);
      }
    }

    //Initilaize for Activity Read
    actData = new ArrayList<>();
    final String rangeAct = "Activities!A2:F";
    response = service.spreadsheets().values().get(spreadsheetId, rangeAct).execute();
    values = response.getValues();

    if (values == null || values.isEmpty()) {
      System.out.println("No data found.");
    } else {
      for (List<Object> row : values) {
        List<String> tempRow = new ArrayList<>();
        if(row.size() == 4){
          row.add("");
        }
        for(int i = 0;i<6;i++){
          tempRow.add(row.get(i).toString());
        }
        actData.add(tempRow);
      }
    }
    Schedule.main(args);

  }
}