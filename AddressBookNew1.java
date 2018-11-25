import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class AddressBookNew1 extends Application implements AddressBookNew1Finals {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AddressBookPane pane = new AddressBookPane();
		Scene scene = new Scene(pane);
		// scene.getStylesheets().add(STYLES_CSS);
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(500);
		primaryStage.setTitle(TITLE);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setAlwaysOnTop(true);
	}
}

class AddressBookPane extends GridPane implements AddressBookNew1Finals, AddressBookEvent1 {
	private RandomAccessFile raf;
	private TextField jtfName = new TextField();
	private TextField jtfStreet = new TextField();
	private TextField jtfCity = new TextField();
	private TextField jtfState = new TextField();
	private TextField jtfZip = new TextField();
	private AddButton jbtAdd;
	private FirstButton jbtFirst;
	private NextButton jbtNext;
	private PreviousButton jbtPrevious;
	private LastButton jbtLast;
	private ClearButton jbtClear;
	private ReverseButton jbtReverse;

	private ComboBox<Integer> switchBox1 = new ComboBox<>();
	private ComboBox<Integer> switchBox2 = new ComboBox<>();
	private int counter = 0;
	private SwitchButton switchButton;

	private Originator originator;
	private CareTaker careTaker;
	private Button btUndo = new Button("Undo");
	private Button btRedo = new Button("Redo");

	// private EventHandler<ActionEvent> ae =
	// e -> ((Command) e.getSource()).Execute();
	private EventHandler<ActionEvent> ae = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			originator.setState(GetName(), jtfStreet.getText(), jtfCity.getText(), jtfState.getText(),
					jtfZip.getText());
			((Command) arg0.getSource()).Execute();
		}
	};

	public AddressBookPane() {
		originator = new Originator();
		careTaker = new CareTaker();
		try {
			raf = new RandomAccessFile(FILE_NAME, FILE_MODE);
		} catch (IOException ex) {
			System.out.println(ex);
			System.exit(0);
		}
		jtfState.setAlignment(Pos.CENTER_LEFT);
		jtfState.setPrefWidth(25);
		jtfZip.setPrefWidth(60);
		jbtAdd = new AddButton(this, raf, careTaker);
		jbtFirst = new FirstButton(this, raf, careTaker);
		jbtNext = new NextButton(this, raf, careTaker);
		jbtPrevious = new PreviousButton(this, raf, careTaker);
		jbtLast = new LastButton(this, raf, careTaker);
		jbtClear = new ClearButton(this, raf, careTaker);
		jbtReverse = new ReverseButton(this, raf, careTaker);
		Label state = new Label(STATE);
		Label zp = new Label(ZIP);
		Label name = new Label(NAME);
		Label street = new Label(STREET);
		Label city = new Label(CITY);
		GridPane p1 = new GridPane();
		p1.add(name, 0, 0);
		p1.add(street, 0, 1);
		p1.add(city, 0, 2);
		p1.setAlignment(Pos.CENTER_LEFT);
		p1.setVgap(8);
		p1.setPadding(new Insets(0, 2, 0, 2));
		GridPane.setVgrow(name, Priority.ALWAYS);
		GridPane.setVgrow(street, Priority.ALWAYS);
		GridPane.setVgrow(city, Priority.ALWAYS);
		GridPane adP = new GridPane();
		adP.add(jtfCity, 0, 0);
		adP.add(state, 1, 0);
		adP.add(jtfState, 2, 0);
		adP.add(zp, 3, 0);
		adP.add(jtfZip, 4, 0);
		adP.setAlignment(Pos.CENTER_LEFT);
		GridPane.setHgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfCity, Priority.ALWAYS);
		GridPane.setVgrow(jtfState, Priority.ALWAYS);
		GridPane.setVgrow(jtfZip, Priority.ALWAYS);
		GridPane.setVgrow(state, Priority.ALWAYS);
		GridPane.setVgrow(zp, Priority.ALWAYS);
		GridPane p4 = new GridPane();
		p4.add(jtfName, 0, 0);
		p4.add(jtfStreet, 0, 1);
		p4.add(adP, 0, 2);
		p4.setVgap(1);
		GridPane.setHgrow(jtfName, Priority.ALWAYS);
		GridPane.setHgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setHgrow(adP, Priority.ALWAYS);
		GridPane.setVgrow(jtfName, Priority.ALWAYS);
		GridPane.setVgrow(jtfStreet, Priority.ALWAYS);
		GridPane.setVgrow(adP, Priority.ALWAYS);
		GridPane jpAddress = new GridPane();
		jpAddress.add(p1, 0, 0);
		jpAddress.add(p4, 1, 0);
		GridPane.setHgrow(p1, Priority.NEVER);
		GridPane.setHgrow(p4, Priority.ALWAYS);
		GridPane.setVgrow(p1, Priority.ALWAYS);
		GridPane.setVgrow(p4, Priority.ALWAYS);
		jpAddress.setStyle(STYLE_COMMAND);
		FlowPane jpButton = new FlowPane();
		jpButton.setHgap(5);
		if (eventType.ADD.getDoEvent())
			jpButton.getChildren().add(jbtAdd);
		if (eventType.FIRST.getDoEvent())
			jpButton.getChildren().add(jbtFirst);
		if (eventType.NEXT.getDoEvent())
			jpButton.getChildren().add(jbtNext);
		if (eventType.PREVIOUS.getDoEvent())
			jpButton.getChildren().add(jbtPrevious);
		if (eventType.LAST.getDoEvent())
			jpButton.getChildren().add(jbtLast);
		if (eventType.CLEAR.getDoEvent())
			jpButton.getChildren().add(jbtClear);
		if (eventType.REVERSE.getDoEvent())
			jpButton.getChildren().add(jbtReverse);
		jpButton.setAlignment(Pos.CENTER);
		GridPane.setVgrow(jpButton, Priority.NEVER);
		GridPane.setVgrow(jpAddress, Priority.ALWAYS);
		GridPane.setHgrow(jpButton, Priority.ALWAYS);
		GridPane.setHgrow(jpAddress, Priority.ALWAYS);
		this.setVgap(5);
		this.add(jpAddress, 0, 0);
		this.add(jpButton, 0, 1);

		switchButton = new SwitchButton(this, raf, careTaker);
		switchBox1.setPrefWidth(70);
		switchBox2.setPrefWidth(70);
		HBox switchTow = new HBox(switchBox1, switchBox2, switchButton);
		switchTow.setPadding(new Insets(5));
		switchTow.setSpacing(15);
		switchTow.setStyle(STYLE_COMMAND);
		switchTow.setAlignment(Pos.CENTER);
		switchButton.setOnAction(ae);
		try {
			setComboBox();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (eventType.SWITCH.getDoEvent())
			this.add(switchTow, 0, 2);

		HBox momentoPane = new HBox(btUndo, btRedo);
		momentoPane.setPadding(new Insets(5));
		momentoPane.setSpacing(15);
		momentoPane.setAlignment(Pos.CENTER);
		this.add(momentoPane, 0, 3);
		btUndo.setOnAction(e -> {
			if (careTaker.thereIsMomento() != 0) {
				originator.getStateFromMemento(careTaker.getPrev());
				setMomento(originator);
			}
		});
		btRedo.setOnAction(e -> {
			if (careTaker.thereIsMomento() != 0) {
				originator.getStateFromMemento(careTaker.getNext());
				setMomento(originator);
			}
		});

		jbtAdd.setOnAction(ae);
		jbtFirst.setOnAction(ae);
		jbtNext.setOnAction(ae);
		jbtPrevious.setOnAction(ae);
		jbtLast.setOnAction(ae);
		jbtClear.setOnAction(ae);
		jbtReverse.setOnAction(ae);
		jbtFirst.Execute();
	}

	public void SetName(String text) {
		jtfName.setText(text);
	}

	public void SetStreet(String text) {
		jtfStreet.setText(text);
	}

	public void SetCity(String text) {
		jtfCity.setText(text);
	}

	public void SetState(String text) {
		jtfState.setText(text);
	}

	public void SetZip(String text) {
		jtfZip.setText(text);
	}

	public String GetName() {
		return jtfName.getText();
	}

	public String GetStreet() {
		return jtfStreet.getText();
	}

	public String GetCity() {
		return jtfCity.getText();
	}

	public String GetState() {
		return jtfState.getText();
	}

	public String GetZip() {
		return jtfZip.getText();
	}

	public void clearTextFields() {
		jtfName.setText("");
		jtfStreet.setText("");
		jtfCity.setText("");
		jtfState.setText("");
		jtfZip.setText("");
	}

	public int GetBox1() {
		return switchBox1.getValue();
	}

	public int GetBox2() {
		return switchBox2.getValue();
	}

	public void AddNew() {
		counter++;
		switchBox1.getItems().add(counter);
		switchBox2.getItems().add(counter);
		if (counter == 1) {
			switchBox1.setValue(1);
			switchBox2.setValue(1);
		}
	}

	public void setComboBox() throws IOException {
		if (raf.length() == 0) {
			counter = 0;
			switchBox1.getItems().clear();
			switchBox2.getItems().clear();
			return;
		} else {
			long numberOfRecords = (raf.length()) / (2 * RECORD_SIZE);
			for (int i = 0; i < numberOfRecords; i++) {
				counter++;
				switchBox1.getItems().add(counter);
				switchBox2.getItems().add(counter);

			}
			switchBox1.setValue(1);
			switchBox2.setValue(1);
		}
	}

	public void setMomento(Originator state) {
		if (state.getName() != null && state.getStreet() != null && state.getCity() != null && state.getState() != null
				&& state.getZip() != null) {
			SetName(state.getName());
			SetStreet(state.getStreet());
			SetCity(state.getCity());
			SetState(state.getState());
			SetZip(state.getZip());
		}
	}

	public void saveMomento(Originator state) {
		careTaker.add(state.saveStateToMemento());
	}

	public Originator getOriginator() {
		return originator;
	}
}

interface Command {
	public void Execute();
}

class CommandButton extends Button implements Command, AddressBookNew1Finals {
	private AddressBookPane p;
	private RandomAccessFile raf;
	private CareTaker careTaker;

	public CommandButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super();
		p = pane;
		raf = r;
		this.careTaker = careTaker;
	}

	public AddressBookPane getPane() {
		return p;
	}

	public RandomAccessFile getFile() {
		return raf;
	}

	public CareTaker getCareTaker() {
		return careTaker;
	}

	public void setPane(AddressBookPane p) {
		this.p = p;
	}

	@Override
	public void Execute() {
	}

	public void writeAddress(long position) {
		try {
			getFile().seek(position);
			FixedLengthStringIO1.writeFixedLengthString(getPane().GetName(), NAME_SIZE, getFile());
			FixedLengthStringIO1.writeFixedLengthString(getPane().GetStreet(), STREET_SIZE, getFile());
			FixedLengthStringIO1.writeFixedLengthString(getPane().GetCity(), CITY_SIZE, getFile());
			FixedLengthStringIO1.writeFixedLengthString(getPane().GetState(), STATE_SIZE, getFile());
			FixedLengthStringIO1.writeFixedLengthString(getPane().GetZip(), ZIP_SIZE, getFile());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void readAddress(long position) throws IOException {
		getFile().seek(position);
		String name = FixedLengthStringIO1.readFixedLengthString(NAME_SIZE, getFile());
		String street = FixedLengthStringIO1.readFixedLengthString(STREET_SIZE, getFile());
		String city = FixedLengthStringIO1.readFixedLengthString(CITY_SIZE, getFile());
		String state = FixedLengthStringIO1.readFixedLengthString(STATE_SIZE, getFile());
		String zip = FixedLengthStringIO1.readFixedLengthString(ZIP_SIZE, getFile());
		getPane().SetName(name);
		getPane().SetStreet(street);
		getPane().SetCity(city);
		getPane().SetState(state);
		getPane().SetZip(zip);
	}
}

class AddButton extends CommandButton {
	public AddButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(ADD);
	}

	@Override
	public void Execute() {
		try {
			getCareTaker().add(getPane().getOriginator().saveStateToMemento());
			System.out.println(getPane().getOriginator().saveStateToMemento().getName());
			writeAddress(getFile().length());
			getPane().AddNew();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class NextButton extends CommandButton {
	public NextButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(NEXT);
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = getFile().getFilePointer();
			if (currentPosition < getFile().length()) {
				readAddress(currentPosition);
				getCareTaker().add(getPane().getOriginator().saveStateToMemento());

			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class PreviousButton extends CommandButton {
	public PreviousButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(PREVIOUS);
	}

	@Override
	public void Execute() {
		try {
			long currentPosition = getFile().getFilePointer();
			if (currentPosition - 2 * 2 * RECORD_SIZE >= 0) {
				readAddress(currentPosition - 2 * 2 * RECORD_SIZE);
				getCareTaker().add(getPane().getOriginator().saveStateToMemento());
			} else
				;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class LastButton extends CommandButton {
	public LastButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(LAST);
	}

	@Override
	public void Execute() {
		try {
			long lastPosition = getFile().length();
			if (lastPosition > 0) {
				readAddress(lastPosition - 2 * RECORD_SIZE);
				getCareTaker().add(getPane().getOriginator().saveStateToMemento());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class FirstButton extends CommandButton {
	public FirstButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(FIRST);
	}

	@Override
	public void Execute() {
		try {
			if (getFile().length() > 0) {
				readAddress(0);
				getCareTaker().add(getPane().getOriginator().saveStateToMemento());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

class ClearButton extends CommandButton {
	public ClearButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(CLEAR);
	}

	@Override
	public void Execute() {
		try {
			getFile().setLength(0);
			getPane().setComboBox();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getPane().clearTextFields();
	}
}

class ReverseButton extends CommandButton {
	public ReverseButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(REVERSE);
	}

	@Override
	public void Execute() {
		try {
			long originalRafLength = getFile().length();
			long numberOfRecords = (getFile().length()) / (2 * RECORD_SIZE);
			if (numberOfRecords <= 1)
				return;
			for (int i = 0; i < numberOfRecords / 2; i++) {
				readAddress(i * 2 * RECORD_SIZE);
				writeAddress(originalRafLength);
				readAddress(originalRafLength - (i + 1) * 2 * RECORD_SIZE);
				writeAddress(i * 2 * RECORD_SIZE);
				readAddress(originalRafLength);
				writeAddress(originalRafLength - (i + 1) * 2 * RECORD_SIZE);
				getFile().setLength(originalRafLength);
			}
			readAddress(0);
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}

class SwitchButton extends CommandButton {
	public SwitchButton(AddressBookPane pane, RandomAccessFile r, CareTaker careTaker) {
		super(pane, r, careTaker);
		this.setText(SWITCH);
	}

	@Override
	public void Execute() {
		try {
			long originalRafLength = getFile().length();
			int sRecord1 = getPane().GetBox1() - 1;
			int sRecord2 = getPane().GetBox2() - 1;
			if (sRecord1 == sRecord2)
				return;
			readAddress(sRecord1 * 2 * RECORD_SIZE);
			writeAddress(originalRafLength);
			readAddress(sRecord2 * 2 * RECORD_SIZE);
			writeAddress(sRecord1 * 2 * RECORD_SIZE);
			readAddress(originalRafLength);
			writeAddress(sRecord2 * 2 * RECORD_SIZE);
			getFile().setLength(originalRafLength);
			readAddress(0);
			return;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}

class Memento {
	private String name;
	private String street;
	private String city;
	private String state;
	private String zip;

	public Memento(String name, String street, String city, String state, String zip) {
		super();
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

}

// ***** CareTaker Class *****///
class CareTaker {
	private List<Memento> mementoList = new ArrayList<Memento>();
	private int index;

	public CareTaker() {
		index = mementoList.size();
	}

	public void add(Memento state) {
		if (state != null) {
			mementoList.add(state);
			index = mementoList.size() - 1;
		}
	}

	public Memento getPrev() {
		if (mementoList.isEmpty() || index <= 0) {
			return null;
		}
		return mementoList.get(--index);
	}

	public Memento getNext() {
		if (mementoList.isEmpty() || index >= mementoList.size() - 1) {
			return null;
		}
		return mementoList.get(++index);
	}

	public int thereIsMomento() {
		return mementoList.isEmpty() == true ? 0 : 1;
	}
}

class Originator {
	private String name;
	private String street;
	private String city;
	private String state;
	private String zip;

	public String getName() {
		return name;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public void setState(String name, String street, String city, String state, String zip) {
		this.name = name;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public Memento saveStateToMemento() {
		return new Memento(name, street, city, state, zip);
	}

	public void getStateFromMemento(Memento memento) {
		if (memento != null) {
			this.name = memento.getName();
			this.street = memento.getStreet();
			this.city = memento.getCity();
			this.state = memento.getState();
			this.zip = memento.getZip();
		}
	}
}
