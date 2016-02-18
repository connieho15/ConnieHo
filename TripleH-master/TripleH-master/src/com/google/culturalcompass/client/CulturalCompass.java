package com.google.culturalcompass.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.culturalcompass.shared.Place;
import com.google.culturalcompass.client.AdminPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.culturalcompass.client.LoginService;
import com.google.culturalcompass.client.LoginServiceAsync;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.ArrayHelper;
import com.google.maps.gwt.client.DirectionsRenderer;
import com.google.maps.gwt.client.DirectionsRendererOptions;
import com.google.maps.gwt.client.DirectionsRequest;
import com.google.maps.gwt.client.DirectionsResult;
import com.google.maps.gwt.client.DirectionsRoute;
import com.google.maps.gwt.client.DirectionsService;
import com.google.maps.gwt.client.DirectionsStatus;
import com.google.maps.gwt.client.GoogleMap;
import com.google.maps.gwt.client.InfoWindow;
import com.google.maps.gwt.client.InfoWindowOptions;
import com.google.maps.gwt.client.LatLng;
import com.google.maps.gwt.client.MapOptions;
import com.google.maps.gwt.client.Marker;
import com.google.maps.gwt.client.MarkerImage;
import com.google.maps.gwt.client.MarkerOptions;
import com.google.maps.gwt.client.MouseEvent;
import com.google.maps.gwt.client.Polyline;
import com.google.maps.gwt.client.PolylineOptions;
import com.google.maps.gwt.client.TravelMode;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.gwt.core.client.Callback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CulturalCompass implements EntryPoint {
	RootLayoutPanel rootPanel;
	MapOptions options;
	GoogleMap mainMap;
	List<Place> allPlace;
	JsArray<DirectionsRoute> route;
	DirectionsService directionService = DirectionsService.create();
	DirectionsRenderer directionDisplay = DirectionsRenderer.create();
	Button showDirectionButton;
	Button removeDirectionButton;
	Button clearTokensButton;
	Button storeButton;
	Marker userMarker;
	Marker destination;


	private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";
	private static final String FACEBOOK_CLIENT_ID = "1506649229662039";
	private static final String FACEBOOK_EMAIL_SCOPE = "email";
	private static final String FACEBOOK_BIRTHDAY_SCOPE = "user_birthday";
	private AdminPanel adminPanel;
	boolean isDefault = true;
	Place defaultPlace = new Place(49.23919, -123.03717);
	ArrayList<String> historyStorePlace = new ArrayList<String>();
	ArrayList<String> myPlacesName = new ArrayList<String>();


	final HorizontalPanel directionButtonPanel = new HorizontalPanel();
	final SimplePanel mapPanel = new SimplePanel();
	final SimplePanel searchPlacePanel = new SimplePanel();
	final SimplePanel selectFilePanel = new SimplePanel();
	final StackLayoutPanel slp = new StackLayoutPanel(Unit.EM);
	final FlexTable hcellTable = new FlexTable();
	ArrayList<Place> myplaces = new ArrayList<Place>();
	

	LoginServiceAsync RegistrationDataService = GWT.create(LoginService.class);

	Boolean isLoggedIn = false;
	String username = null;
	String password = null;
	Button logoutButton;
	Button historyButton;

	final PopupPanel loginPopUpPanel = new PopupPanel(true);
	Button facebookButton = new Button("Login with Facebook");
	// private Map<String, String> nameAndPassword;

	private static final Auth AUTH = Auth.get();
	private Place selectedplace;

	PlaceServiceAsync placeService = GWT.create(PlaceService.class);
	PlaceProviderServiceAsync placeProvider = GWT
			.create(PlaceProviderService.class);
	private Button removeButton;

	class PlaceCallBack implements AsyncCallback<Place> {

		@Override
		public void onFailure(Throwable caught) {
			Window.alert("Unable to obtain server response: "
					+ caught.getMessage());
		}

		@Override
		public void onSuccess(Place result) {
			Window.alert(result.getName());
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		slp.setSize("39em", "35em");
		rootPanel = RootLayoutPanel.get();
		rootPanel.setStyleName("root");
		
		createLoginView();
		loadAdminPanel();
		
		initMap();
		addDirectionButton();
		// addFacebookAuth();
		Auth.export();
		// addClearTokens();
		loadMyPlace();
		loadAllPlaces();
		if (isLoggedIn = true){
			createHistoryTable();
		}

		
		rootPanel.add(slp);
		rootPanel.setWidgetLeftRight(slp, 20, Unit.PX, 700, Unit.PX);
		rootPanel.setWidgetTopBottom(slp, 100, Unit.PX, 0, Unit.PX);
		rootPanel.setSize("100%", "100%");

	}

	

	private void loadAllPlaces() {
		placeProvider.getPlaceProviders(new AsyncCallback<Place[]>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("fail to parse data");
			}

			@Override
			public void onSuccess(Place[] result) {
				// Window.alert("YEAAAAAA");
				List<Place> places = new ArrayList<Place>();
				for (int i = 0; i < result.length; i++) {
					places.add(result[i]);
				}
				loadPlace(places);
			}
		});
	}

	private void loadPlace(List<Place> places) {
		allPlace = places;
		createSideBar();
	}

	// anastasia

	private void createLoginView() {
		Button facebookButton = new Button("Login with Facebook");
		facebookButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL,
						FACEBOOK_CLIENT_ID).withScopes(FACEBOOK_EMAIL_SCOPE,
								FACEBOOK_BIRTHDAY_SCOPE).withScopeDelimiter(",");
				AUTH.login(req, new Callback<String, Throwable>() {
					@Override
					public void onSuccess(String token) {
						Window.alert("Successfully logged in with facebook");
						loginPopUpPanel.hide();
						logoutButton = new Button("Logout");
						logoutButton.setWidth("45px");
						logoutButton
						.addClickHandler(new ClickHandler() {
							public void onClick(
									ClickEvent event) {
								Window.Location.reload();
								isLoggedIn = false;
								createLoginView();
								if (!isLoggedIn) {
									rootPanel
									.remove(logoutButton);
									createLoginView();
								}
							}
						});

						rootPanel.add(logoutButton);
						rootPanel.setWidgetLeftWidth(logoutButton,
								15.0, Unit.PX, 75.0, Unit.PX);

						rootPanel.setWidgetBottomHeight(
								logoutButton, 10.0, Unit.PX, 45.0,
								Unit.PX);
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error:\n" + caught.getMessage());
					}
				});
			}
		});

		// first popup a panel for login() start

		loginPopUpPanel.setSize("400px", "200px");

		FlexTable flexTable = new FlexTable();
		flexTable.setSize("300px", "150px");

		Label usernameLabel = new Label("Username:");
		final TextBox usernameTextbox = new TextBox();
		Label passwordLabel = new Label("Password:");
		final PasswordTextBox passwordTextbox = new PasswordTextBox();

		// Label facebookLabel = new Label("Login with Facebook");

		Button loginButton = new Button("Login");
		Label newuserLabel = new Label("New to CulturalCompass?");
		Button moveToRegisterButton = new Button("Register Now");
		flexTable.setWidget(0, 0, usernameLabel);
		flexTable.setWidget(0, 1, usernameTextbox);
		flexTable.setWidget(1, 0, passwordLabel);
		flexTable.setWidget(1, 1, passwordTextbox);
		flexTable.setWidget(2, 1, loginButton);
		flexTable.setWidget(3, 0, newuserLabel);
		flexTable.setWidget(3, 1, moveToRegisterButton);
		flexTable.setWidget(4, 0, facebookButton);
		// flexTable.setWidget(4, 1, facebookButton);
		// /for new user to register button
		moveToRegisterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginPopUpPanel.hide();
				createRegisterView();
			}
		});
		// /for login button
		loginButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				username = usernameTextbox.getText();
				password = passwordTextbox.getText();
				if (password.length() > 0 && username.length() > 0) {
					RegistrationDataService.login(username, password,
							new AsyncCallback<Boolean>() {

						public void onFailure(Throwable caught) {
							showLoginMessage("Warning!",
									"Could not connect to server");
						}

						public void onSuccess(Boolean success) {
							if (success) {
								isLoggedIn = true;
								showLoginMessage("Welcome!",
										"Logged in as \"" + username
										+ "\"successfully!");
								loginPopUpPanel.hide();
								addLogoutButton();
							} else {
								username = null;
								isLoggedIn = false;
								showLoginMessage("Warning!",
										"Sorry, the password and/or username you entered was wrong. Please try again.");
							}
						}

						private void addLogoutButton() {
							logoutButton = new Button("Logout");
							logoutButton.setWidth("45px");
							logoutButton
							.addClickHandler(new ClickHandler() {
								public void onClick(
										ClickEvent event) {
									Window.Location.reload();
									isLoggedIn = false;
									createLoginView();
									if (!isLoggedIn) {
										rootPanel
										.remove(logoutButton);
										createLoginView();
									}
								}
							});

							rootPanel.add(logoutButton);
							loginPopUpPanel.hide();
							rootPanel.setWidgetLeftWidth(logoutButton,
									15.0, Unit.PX, 75.0, Unit.PX);

							rootPanel.setWidgetBottomHeight(
									logoutButton, 10.0, Unit.PX, 45.0,
									Unit.PX);


						}
					});
				} else {
					showLoginMessage("Warning!",
							"Please provide your username and/or password.");
				}
			}

			private void showLoginMessage(String text, String message) {
				VerticalPanel messagePanel = new VerticalPanel();
				final DialogBox dialog = new DialogBox(false);
				dialog.add(messagePanel);
				dialog.center();
				dialog.show();
				dialog.setGlassEnabled(true);
				dialog.setText(text);

				Label loginMessage = new Label(message);
				messagePanel.add(loginMessage);

				Button closeButton = new Button("Close", new ClickHandler() {
					public void onClick(ClickEvent event) {
						dialog.hide();
					}
				});
				messagePanel.add(closeButton);
			}
		});

		// /for login button
		// ////for regidterButton

		moveToRegisterButton = new Button("Register");
		moveToRegisterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loginPopUpPanel.setAutoHideEnabled(false);///@
				createRegisterView();
			}
		});

		// ////////for registerButton
		loginPopUpPanel.add(flexTable);
		// the background will be blocked with a semi-transparent pane
		loginPopUpPanel.setGlassEnabled(true);
		loginPopUpPanel.setAutoHideEnabled(false);
		loginPopUpPanel.center();
		loginPopUpPanel.show();
		// //////first popup a panel for login() end

	}

	private void createRegisterView() {
		Label usernameLabel = new Label("Username:");
		Label passwordLabel = new Label("Password:");
		Label confirmLabel = new Label("Confirm Password:");
		Label emailLabel = new Label("Email:");
		Label genderLabel = new Label("Gender:");
		Label questionLabel = new Label("Select reminder question");
		Label answerLabel = new Label("Answer:");

		final TextBox usernameTextbox = new TextBox();
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		final PasswordTextBox confirmPasswordTextbox = new PasswordTextBox();
		final TextBox emailTextbox = new TextBox();
		final ListBox genderListbox = new ListBox();
		genderListbox.addItem("female");
		genderListbox.addItem("male");
		final ListBox questionListbox = new ListBox();
		questionListbox.addItem("what is your mother's maiden name?");
		questionListbox.addItem("what city were you born in?");
		questionListbox.addItem("what was the name of your first pet?");
		questionListbox.addItem("what is your favourite sports team?");
		final TextBox answerTextbox = new TextBox();

		Button cancelButton = new Button("Cancel");
		Button registerButton = new Button("Register");

		final PopupPanel registerPopUpPanel = new PopupPanel(true);
		registerPopUpPanel.setSize("400px", "500px");

		FlexTable flexTable = new FlexTable();
		flexTable.setSize("300px", "115px");
		flexTable.setWidget(0, 0, usernameLabel);
		flexTable.setWidget(0, 1, usernameTextbox);
		flexTable.setWidget(1, 0, passwordLabel);
		flexTable.setWidget(1, 1, passwordTextBox);
		flexTable.setWidget(2, 0, confirmLabel);
		flexTable.setWidget(2, 1, confirmPasswordTextbox);
		flexTable.setWidget(3, 0, emailLabel);
		flexTable.setWidget(3, 1, emailTextbox);
		flexTable.setWidget(4, 0, genderLabel);
		flexTable.setWidget(4, 1, genderListbox);
		flexTable.setWidget(5, 0, questionLabel);
		flexTable.setWidget(5, 1, questionListbox);
		flexTable.setWidget(6, 0, answerLabel);
		flexTable.setWidget(6, 1, answerTextbox);
		flexTable.setWidget(7, 0, cancelButton);
		flexTable.setWidget(7, 1, registerButton);

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				registerPopUpPanel.hide();
				//createLoginView();
				loginPopUpPanel.center();
				loginPopUpPanel.show();
				loginPopUpPanel.setAutoHideEnabled(false);
			}
		});

		registerButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final String username = usernameTextbox.getText();
				String password = passwordTextBox.getText();
				String confirmPassword = confirmPasswordTextbox.getText();
				String email = emailTextbox.getText();
				String gender = genderListbox.getItemText(genderListbox
						.getSelectedIndex());
				String reminderQuestion = questionListbox
						.getItemText(questionListbox.getSelectedIndex());
				String answer = answerTextbox.getText();
				if (!password.matches("^[0-9A-Z&#92;&#92;.]{1,10}$")) {
					showRegisterMessage(
							"Warning!",
							"Your passwords must be between 1 and 10 chars that are numbers, letters, or dots.");

				}
				if (!password.equals(confirmPassword)) {
					showRegisterMessage("Warning!",
							"Oops - those passwords don't match.");

				}
				// validate email address
				if (!email
						.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) 
				{
					showRegisterMessage("Warning!",
							"Email address is not formatted correctly.");

				}

				if (username.length() > 0 && password.length() > 0) {
					RegistrationDataService.register(username, password, email,
							gender, reminderQuestion, answer,
							new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							showRegisterMessage("Warning!",
									"Could not connect to server.");

						}

						public void onSuccess(Boolean success) {
							if (success) {
								loginPopUpPanel.center();
								loginPopUpPanel.show();
								loginPopUpPanel.setAutoHideEnabled(false);
								showRegisterMessage("Welcome!",
										"You've register \""
												+ username
												+ "\"successfully!");
								registerPopUpPanel.hide();
							} else {
								showRegisterMessage(
										"Warning!",
										"This username \""
												+ username
												+ "\" is already in use.");
							}
						}


					});
				} else {
					showRegisterMessage("Warning!",
							"Username and password are required");
				}
			}
//			private void addLogoutButton() {
//
//				logoutButton = new Button("Logout");
//				logoutButton.setWidth("45px");
//				logoutButton.addStyleName("allButton");
//				logoutButton
//				.addClickHandler(new ClickHandler() {
//					public void onClick(
//							ClickEvent event) {
//						isLoggedIn = false;
//						createLoginView();
//						if (!isLoggedIn) {
//							rootPanel
//							.remove(logoutButton);
//							createLoginView();
//						}
//
//					}
//				});
//				rootPanel.add(logoutButton);
//				registerPopUpPanel.hide();
//				rootPanel.setWidgetLeftWidth(logoutButton,
//						15.0, Unit.PX, 75.0, Unit.PX);
//
//				rootPanel.setWidgetBottomHeight(
//						logoutButton, 10.0, Unit.PX, 45.0,
//						Unit.PX);
//
//
//			}


			private void showRegisterMessage(String text, String message) {
				VerticalPanel messagePanel = new VerticalPanel();
				final DialogBox dialog = new DialogBox(false);

				dialog.add(messagePanel);
				dialog.center();
				dialog.show();
				dialog.setAutoHideEnabled(false);
				dialog.setText(text);
				Label registerMessage = new Label(message);
				messagePanel.add(registerMessage);
				Button closeButton = new Button("Close", new ClickHandler() {
					public void onClick(ClickEvent event) {
						dialog.hide();
					}
				});
				messagePanel.add(closeButton);

			}
		});

		registerPopUpPanel.add(flexTable);
		// the background will be blocked with a semi-transparent pane
		registerPopUpPanel.setGlassEnabled(true);
		registerPopUpPanel.setAutoHideEnabled(false);
		registerPopUpPanel.center();
		registerPopUpPanel.show();
	}

	/*
	 * private Button createHistoryButton() { Button HistoryButton = new
	 * Button("Get my List of Places!", new ClickHandler() { public void
	 * onClick(ClickEvent event) { Window.alert("Not Implemented"); } });
	 * HistoryButton.setStyleName("allButton"); slp.add(HistoryButton); return
	 * HistoryButton; }
	 */

	private void loadAdminPanel() {
		adminPanel = AdminPanel.getInstance();
		rootPanel.add(adminPanel);
		adminPanel.setPanel(rootPanel);
	}

	public void initMap() {
		options = MapOptions.create();
		options.setCenter(LatLng.create(49.23919, -123.03717));
		options.setZoom(11);
		options.setDraggable(true);
		options.setMapTypeControl(true);
		options.setScaleControl(true);
		options.setScaleControl(true);
		options.setScrollwheel(true);
		options.setStreetViewControl(false);

		rootPanel.add(mapPanel);
		rootPanel.setWidgetLeftRight(mapPanel, 600.0, Unit.PX, 0.0, Unit.PX);
		rootPanel.setWidgetTopBottom(mapPanel, 100.0, Unit.PX, 0.0, Unit.PX);
		mapPanel.setSize("650px", "480px");

		mainMap = GoogleMap.create(mapPanel.getElement(), options);

		userMarker = addMarker(defaultPlace);
		destination = addMarker(defaultPlace);
		destination.setVisible(false);
		mainMap.addClickListener(new GoogleMap.ClickHandler() {

			@Override
			public void handle(MouseEvent event) {
				userMarker.setPosition(event.getLatLng());
				directionDisplay.setMap(null);
			}
		});
	}


	private void addDirectionButton() {
		showDirectionButton = new Button("Get Direction");
		showDirectionButton.setStyleName("allButton");
		showDirectionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mapRoute(userMarker.getPosition(), destination.getPosition());
			}
		});

		removeDirectionButton = new Button("Remove Route");
		removeDirectionButton.setStyleName("allButton");
		removeDirectionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				directionDisplay.setMap(null);
			}
		});

		showDirectionButton.setWidth("100px");
		removeDirectionButton.setWidth("128px");
		directionButtonPanel.add(showDirectionButton);
		directionButtonPanel.add(removeDirectionButton);
		
	}

	private void mapRoute(LatLng start, LatLng end) {
		if (start.equals(end)) {
			return;
		} else {
			DirectionsRequest req = DirectionsRequest.create();
			req.setOrigin(start);
			req.setDestination(end);
			req.setTravelMode(TravelMode.DRIVING);
			req.setOptimizeWaypoints(true);

			directionService.route(req, new DirectionsService.Callback() {

				@Override
				public void handle(DirectionsResult respond, DirectionsStatus status) {
					if (status == DirectionsStatus.OK) {
						DirectionsRendererOptions opt = DirectionsRendererOptions
								.create();
						opt.setSuppressMarkers(true);
						opt.setDirections(respond);
						opt.setMap(mainMap);

						directionDisplay.setOptions(opt);
					} else {
						Window.alert("Directions fail");
					}
				}
			});
		}
	}

	private Marker addMarker(final Place p) {
		LatLng latlng = p.getLatLng();
		final String name = p.getName();
		MarkerOptions markerOption = MarkerOptions.create();
		markerOption.setPosition(latlng);
		if (name == null) {
			MarkerImage icon = MarkerImage
					.create("http://maps.gstatic.com/mapfiles/cb/man_arrow-0.png");			
					markerOption.setIcon(icon);
					Marker marker = Marker.create(markerOption);
					marker.setMap(mainMap);

					return marker;
		} else {
			markerOption.setTitle(name);
			markerOption.setCursor(name);
			markerOption.setClickable(true);
			MarkerImage icon = MarkerImage
					.create("http://www.google.com/mapfiles/marker.png");
			markerOption.setIcon(icon);
			markerOption.setAnimation(Animation.DROP);

			final Marker marker = Marker.create(markerOption);
			marker.setMap(mainMap);
			marker.addClickListener(new Marker.ClickHandler() {

				@Override
				public void handle(MouseEvent event) {
					InfoWindow info = InfoWindow.create();
					//boolean toStore = true;
					String contentString = "<b>" + p.getName() + "</b> <br/>" + p.getAddress();
					info.setContent(contentString);
					info.open(mainMap, marker);
					destination = marker;
					//for (Place place: historyStorePlace) {
						//if (place.getLatLng().equals(p.getLatLng())) {
							//toStore = false;
						//}
					//}
//					if (toStore) {
//						historyStorePlace.add(p.getName());
//						}
//					
						}
			});
			
			return marker;
		}
	}

	
	private void createSideBar() {
		final TextBox txtName = new TextBox();
		VerticalPanel mainPanel = new VerticalPanel();
		ScrollPanel scrollPanel = new ScrollPanel();
		final FlexTable headerTable = new FlexTable();
		final FlexTable cellTable = new FlexTable();

		displayDefaultCell(headerTable);
		displayCells(cellTable, allPlace);

		txtName.setWidth("200");
		txtName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					searchPlaceService(cellTable, txtName.getValue());
				}
			}
		});

		Button buttonMessage = new Button("Search");
		buttonMessage.setStyleName("allButton");

		buttonMessage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchPlaceService(cellTable, txtName.getValue());
			}
		});

		storeButton = new Button("Store Selected Places");
		storeButton.setStyleName("allButton");

		storeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				RegistrationDataService.addPlace(username, historyStorePlace,
						new AsyncCallback<Boolean>() {

					public void onFailure(Throwable caught) {
						Window.alert(
								"Could not connect to server");
					}

					public void onSuccess(Boolean success) {
						if (success) {
							isLoggedIn = true;
							Window.alert(historyStorePlace.size() + " Places Stored!");
						}

					}
				});
			}

		});
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.add(txtName);
		hPanel.add(buttonMessage);
		

		cellTable.setBorderWidth(1);
		cellTable.setCellSpacing(1);
		cellTable.setHeight("20px");
		cellTable.setPixelSize(500, 500);

		scrollPanel.setHeight("400px");
		scrollPanel.add(cellTable);
		mainPanel.add(storeButton);
		mainPanel.add(directionButtonPanel);
		mainPanel.add(hPanel);
		mainPanel.add(headerTable);
		mainPanel.add(scrollPanel);
		slp.add(mainPanel, new HTML("My Places"), 3);

	}

	private void displayDefaultCell(final FlexTable cellTable) {
		cellTable.setCellPadding(6);
		cellTable.getRowFormatter().addStyleName(0, "sidebarHeader");
		cellTable.getCellFormatter().addStyleName(0, 0, "sidebarHeaderOne");
		cellTable.getCellFormatter().addStyleName(0, 1, "sidebarHeader");
		cellTable.getCellFormatter().addStyleName(0, 2, "sidebarHeader");

		cellTable.setText(0, 0, "");
		cellTable.setText(0, 1, "Name");
		cellTable.setText(0, 2, "Address");
	}

	private void displayCells(FlexTable cellTable, List<Place> places) {
		if (!isDefault) {
			cellTable.removeAllRows();
		}

		for (final Place place : places) {
			final Marker m = addMarker(place);
			m.setVisible(false);
			int row = cellTable.getRowCount();
			cellTable.setText(row, 1, place.getName());
			cellTable.setText(row, 2, place.getAddress());
			cellTable.getCellFormatter().addStyleName(row, 0,
					"sidebarColumnOne");
			cellTable.getCellFormatter().addStyleName(row, 1, "sidebarColumn");
			cellTable.getCellFormatter().addStyleName(row, 2, "sidebarColumn");

			CheckBox checkbox = new CheckBox();
			checkbox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					m.setVisible(((CheckBox) event.getSource()).getValue());
					if (destination.getPosition().equals(place.getLatLng())){
						if (!((CheckBox) event.getSource()).getValue() && 
								destination.getPosition().equals(place.getLatLng())){
							directionDisplay.setMap(null);
							destination = addMarker(defaultPlace);
							destination.setVisible(false);
						}
					} else {
						if (((CheckBox) event.getSource()).getValue()){
							destination = m;
						}
					}
					if (((CheckBox) event.getSource()).getValue() && 
							!historyStorePlace.contains(place.getName()) &&
							!myPlacesName.contains(place.getName())) {
						historyStorePlace.add(place.getName());
					}
					if (!((CheckBox) event.getSource()).getValue()) {
						historyStorePlace.remove(place.getName());
					}
					selectedplace = new Place();
					selectedplace.setLat(m.getPosition().lat());
					selectedplace.setLon(m.getPosition().lng());
				}
			});

			checkbox.setSize("10px", "10px");
			cellTable.setWidget(row, 0, checkbox);
		}
	}

	private void searchPlaceService(final FlexTable cellTable,
			final String input) {
		if (input == null && isDefault) {
			return;
		} else {
			isDefault = false;
			getPlaceServiceInstance().searchingPlace(input,
					new AsyncCallback<List<Place>>() {
				public void onFailure(Throwable caught) {
					// Reset the button text and remove the loading
					// animation
					Window.alert("Unable to obtain server response: "
							+ caught.getMessage());
				}

				public void onSuccess(List<Place> result) {
					if (result.size() == 0) {
						Window.alert("no result");
					}
					displayCells(cellTable, result);
				}
			});
			if (input == null) {
				isDefault = true;
			}
		}
	}

	private PlaceServiceAsync getPlaceServiceInstance() {
		if (placeService == null) {
			placeService = (PlaceServiceAsync) GWT.create(PlaceService.class);
			// Specify the URL at which the service implementation is running.
			// The target URL must reside on the same domain and port from
			// which the host page was served.
			((ServiceDefTarget) placeService).setServiceEntryPoint(GWT
					.getModuleBaseURL() + "/CulturalCompass/PlaceService");
		}
		return placeService;
	}

	private void createHistoryTable() {
		VerticalPanel mainPanel = new VerticalPanel();
		ScrollPanel scrollPanel = new ScrollPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();
		final FlexTable headerTable = new FlexTable();

		displayDefaultCell(headerTable);


		hcellTable.setBorderWidth(1);
		hcellTable.setCellSpacing(1);
		hcellTable.setHeight("20px");
		hcellTable.setPixelSize(500, 500);
		scrollPanel.setHeight("400px");
		scrollPanel.add(hcellTable);

		
		Button historyButton = new Button("Get my Saved Places");
		historyButton.setStyleName("allButton");
		historyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showUserHistory();
			}
		});


		/*	//cellTable.setText(row, 1, place.getName());
		//cellTable.setText(row, 2, place.getAddress());
		cellTable.getCellFormatter().addStyleName(row, 0,
				"sidebarColumnOne");
		cellTable.getCellFormatter().addStyleName(row, 1, "sidebarColumn");
		cellTable.getCellFormatter().addStyleName(row, 2, "sidebarColumn");

		CheckBox checkbox = new CheckBox();
		checkbox.addClickHandler(new ClickHandler() {
			Marker m = addMarker("My Saved Place", place.getLatLng());
			@Override
			public void onClick(ClickEvent event) {
				m.setVisible(((CheckBox) event.getSource()).getValue());
				selectedplace = new Place();
				selectedplace.setLat(m.getPosition().lat());
				selectedplace.setLon(m.getPosition().lng());
			}
		});

		checkbox.setSize("10px", "10px");
		cellTable.setWidget(row, 0, checkbox);

		 */
		
		removeButton = new Button("Remove Saved Places");
		
		removeButton.setStyleName("allButton");

		removeButton.addClickHandler(new ClickHandler() {
		@Override
		public void onClick(ClickEvent event){
			removePlaces();
		}
		});
		
		buttonPanel.add(historyButton);
		buttonPanel.add(removeButton);
		mainPanel.add(buttonPanel);
		mainPanel.add(headerTable);
		mainPanel.add(scrollPanel);
		
		

		slp.add(mainPanel, new HTML("My Saved Places"), 3);
	}

	private void removePlaces(){
		if (isLoggedIn = true){
			RegistrationDataService.removePlaces(username, new AsyncCallback<Boolean>(){
				public void onFailure(Throwable caught){
					Window.alert("Could not connect to server");
				}
				public void onSuccess(Boolean success){
					historyStorePlace.clear();
					myplaces.clear();
					hcellTable.removeAllRows();
					Window.alert("All User Places Removed!");
				}
				}
			);
			myPlacesName.clear();
			}
		}
	
	private void showUserHistory() {
		if (isLoggedIn = true){

			loadMyPlace();
			displayCells(hcellTable, myplaces);
			}

	}



	private void loadMyPlace() {
		RegistrationDataService.showmyPlace(username,
				new AsyncCallback<ArrayList<String>>() {

			public void onFailure(Throwable caught) {
				//Window.alert(
					//	"Could not connect to server");
			}

			public void onSuccess(ArrayList<String> places) {
				for (String s: places) {
				for (Place p: allPlace){
					if (p.getName().equalsIgnoreCase(s)){
						myplaces.add(p);}
				}
				myPlacesName.add(s);
				}
				
		}	
				}
			);
	}
}
