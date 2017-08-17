package com.google.culturalcompass.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.culturalcompass.client.PlaceService;
import com.google.culturalcompass.client.PlaceServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.dom.client.Style.Unit;
import com.google.culturalcompass.client.LoginInfo;
import com.google.culturalcompass.client.LoginService;
import com.google.culturalcompass.client.LoginServiceAsync;
import com.google.culturalcompass.shared.Place;

public class AdminPanel extends VerticalPanel {

	RootLayoutPanel rootPanel;
	private PlaceProviderServiceAsync ppService = GWT
			.create(PlaceProviderService.class);
	private PlaceServiceAsync dataService;
	private static final AdminPanel INSTANCE = new AdminPanel();
	private static LoginInfo loginInfo = null;
	private VerticalPanel mainPanel = new VerticalPanel();
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("");
	private Anchor signInLink = new Anchor("Sign in as admin");
	private Anchor signOutLink = new Anchor("Sign Out");
	private ArrayList<Place> allPlaces = new ArrayList<Place>();
	private ArrayList<Place> somePlaces = new ArrayList<Place>();
	private FlexTable cellTable = new FlexTable();
	private Button cellTableButton = new Button("IMPORT DATA");
	
	public AdminPanel() {
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.loginAdmin(GWT.getHostPageBaseURL(),
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						if (loginInfo.isLoggedIn() && loginInfo.isAdmin()) {
							loadImportView();
						} else {
							loadLogin();
						}
					}
				});
	}

	private void callImportData() {
		dataService = GWT.create(PlaceService.class);
		dataService.importData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Failed to import data: " + error.toString());
			}
			public void onSuccess(Void Result) {
				getAllPlaces();
				Window.alert("Successfully imported data.");
			}
		});
	}

	public static AdminPanel getInstance() {
		return INSTANCE;
	}

	public void setPanel(RootLayoutPanel root) {
		rootPanel = root;
	}

	public void getAllPlaces() {
		ppService.getPlaceProviders(new AsyncCallback<Place[]>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(Place[] result) {
				allPlaces = new ArrayList<Place>(Arrays.asList(result));
				setSomePlaces(allPlaces);
				displayPlaces();
			}
		});
	}

	public void displayPlaces() {
		displayP(somePlaces);
	}

	public void setSomePlaces(ArrayList<Place> somePlaces) {
		this.somePlaces = new ArrayList<Place>(somePlaces);
	}

	public ArrayList<Place> getEveryPlace() {
		return allPlaces;
	}

	private void loadLogin() {
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		rootPanel.add(loginPanel);
		rootPanel.setWidgetLeftRight(loginPanel, 1200, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(loginPanel, 70, Unit.PX, 500, Unit.PX);
	}

	private void loadImportView() {
		Window.alert("Successfully logged in as admin. Please scroll down to import data.");
		signOutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signOutLink);
		RootPanel.get().add(mainPanel);
		rootPanel.add(mainPanel);
		rootPanel.setWidgetLeftRight(mainPanel, 1200, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(mainPanel, 70, Unit.PX, 500, Unit.PX);
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp0 = new HorizontalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel hp2 = new HorizontalPanel();
		RootPanel.get().add(cellTableButton);
		rootPanel.add(cellTableButton);
		rootPanel.setWidgetLeftRight(cellTableButton, 330, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(cellTableButton, 100, Unit.PX, -300,
				Unit.PX);

		cellTableButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				callImportData();
			}
		});
		hp.add(cellTableButton);
		hp0.add(hp);
		hp0.add(hp2);
		vp.setCellHorizontalAlignment(hp, HorizontalPanel.ALIGN_RIGHT);
		hp0.add(cellTable);
		rootPanel.add(hp0);
		rootPanel.setWidgetLeftRight(hp0, 330, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(hp0, 770, Unit.PX, -800, Unit.PX);
	}

	private void displayP(List<Place> places) {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		mainPanel.add(signOutLink);
		RootPanel.get().add(mainPanel);
		rootPanel.add(mainPanel);
		rootPanel.setWidgetLeftRight(mainPanel, 1200, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(mainPanel, 70, Unit.PX, 500, Unit.PX);
		cellTable.setWidth("1024px");
		cellTable.addStyleName("placeList");
		cellTable.setCellPadding(5);
		cellTable.setText(0, 0, "Name");
		cellTable.setText(0, 1, "Address");
		cellTable.setText(0, 2, "Website");
		cellTable.getColumnFormatter().setWidth(0, "100px");
		cellTable.getColumnFormatter().setWidth(1, "100px");
		cellTable.getColumnFormatter().setWidth(2, "315px");
		int numRows = cellTable.getRowCount();
		for (int i = 1; i < numRows; i++) {
			cellTable.removeRow(1);
		}
		for (Place place : places)
			showPlace(place);
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel hp = new HorizontalPanel();
		HorizontalPanel hp2 = new HorizontalPanel();
		//Button cellTableButton = new Button("Import Data");
		rootPanel.setWidgetLeftRight(cellTableButton, 330, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(cellTableButton, 100, Unit.PX, -300,
				Unit.PX);
		hp.add(cellTableButton);
		vp.add(hp);
		vp.add(hp2);
		vp.setCellHorizontalAlignment(hp, HorizontalPanel.ALIGN_RIGHT);
		vp.add(cellTable);
		rootPanel.add(vp);
		rootPanel.setWidgetLeftRight(vp, 330, Unit.PX, 0, Unit.PX);
		rootPanel.setWidgetTopBottom(vp, 770, Unit.PX, -1200, Unit.PX);
		rootPanel.setSize("100%", "100%");
	}

	private void showPlace(Place place) {
		int row = cellTable.getRowCount();
		cellTable.setText(row, 0, place.getName());
		cellTable.setText(row, 1, place.getAddress());
		cellTable.setText(row, 2, place.getWebsite());
		cellTable.getCellFormatter().addStyleName(row, 0, "Name Column");
		cellTable.getCellFormatter().addStyleName(row, 1, "Address Column");
		cellTable.getCellFormatter().addStyleName(row, 2, "Website Column");
	}

}
