package com.liferay.docs.guestbook.portlet.portlet;

import com.liferay.docs.guestbook.model.Entry;
import com.liferay.docs.guestbook.model.Guestbook;
import com.liferay.docs.guestbook.portlet.constants.GuestbookPortletKeys;

import com.liferay.docs.guestbook.portlet.executor.GuestbookEntryBackgroundTaskExecutor;
import com.liferay.docs.guestbook.service.EntryLocalService;
import com.liferay.docs.guestbook.service.GuestbookLocalService;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskManagerUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author root322
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.social",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Guestbook",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/guestbookwebportlet/view.jsp",
		"javax.portlet.name=" + GuestbookPortletKeys.GUESTBOOK,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class GuestbookPortlet extends MVCPortlet {

	@Reference(unbind = "-")
	protected void setEntryService(EntryLocalService entryLocalService) {
		_entryLocalService = entryLocalService;
	}

	@Reference(unbind = "-")
	protected void setGuestbookService(GuestbookLocalService guestbookLocalService) {
		_guestbookLocalService = guestbookLocalService;
	}

	private EntryLocalService _entryLocalService;
	private GuestbookLocalService _guestbookLocalService;

	@Override
	public void render(RenderRequest renderRequest, RenderResponse renderResponse)
			throws PortletException, IOException {

		try {
			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					Guestbook.class.getName(), renderRequest);

			long groupId = serviceContext.getScopeGroupId();

			long guestbookId = ParamUtil.getLong(renderRequest, "guestbookId");

			List<Guestbook> guestbooks = _guestbookLocalService.getGuestbooks(
					groupId);

			if (guestbooks.isEmpty()) {
				Guestbook guestbook = _guestbookLocalService.addGuestbook(
						serviceContext.getUserId(), "Main", serviceContext);

				guestbookId = guestbook.getGuestbookId();
			}

			if (guestbookId == 0) {
				guestbookId = guestbooks.get(0).getGuestbookId();
			}

			renderRequest.setAttribute("guestbookId", guestbookId);
			renderRequest.setAttribute("guestbooks", guestbooks);
		}
		catch (Exception e) {
			throw new PortletException(e);
		}

		super.render(renderRequest, renderResponse);

//		PortletPreferences prefs = renderRequest.getPreferences();
//		String[] guestbookEntries = prefs.getValues("guestbook-entries", new String[1]);
//
//		if (guestbookEntries[0] != null) {
//			List<Entry> entries = parseEntries(guestbookEntries);
//			renderRequest.setAttribute("entries", entries);
//		}
//
//		super.render(renderRequest, renderResponse);
	}

//	private List<Entry> parseEntries(String[] guestbookEntries) {
//		List<Entry> entries = new ArrayList<Entry>();
//
////		for (String entry : guestbookEntries) {
////			String[] parts = entry.split("\\^", 2);
////			Entry gbEntry = new Entry(parts[0], parts[1]);
////			entries.add(gbEntry);
////		}
//
//		return entries;
//	}

	public void addEntry(ActionRequest request, ActionResponse response) throws PortalException {

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				Entry.class.getName(), request);

		String userName = ParamUtil.getString(request, "name");
		String email = ParamUtil.getString(request, "email");
		String message = ParamUtil.getString(request, "message");
		long guestbookId = Long.parseLong(ParamUtil.getString(request, "guestbook"));
		long entryId = ParamUtil.getLong(request, "entryId");

		if (entryId > 0) {

			try {

				_entryLocalService.updateEntry(
						serviceContext.getUserId(), guestbookId, entryId, userName,
						email, message, serviceContext);

				SessionMessages.add(request, "entryAdded");

				response.setRenderParameter(
						"guestbookId", Long.toString(guestbookId));

			} catch (Exception e) {
				System.out.println(e);

				SessionErrors.add(request, e.getClass().getName());

				PortalUtil.copyRequestParameters(request, response);

				response.setRenderParameter(
						"mvcPath", "/guestbookwebportlet/edit_entry.jsp");
			}

		} else {

			try {
				_entryLocalService.addEntry(
						serviceContext.getUserId(), guestbookId, userName, email,
						message, serviceContext);

				SessionMessages.add(request, "entryAdded");

				response.setRenderParameter(
						"guestbookId", Long.toString(guestbookId));

			} catch (Exception e) {
				SessionErrors.add(request, e.getClass().getName());

				PortalUtil.copyRequestParameters(request, response);

				response.setRenderParameter(
						"mvcPath", "/guestbookwebportlet/edit_entry.jsp");
			}
		}

//		try {
//			PortletPreferences prefs = request.getPreferences();
//
//			String[] guestbookEntries = prefs.getValues("guestbook-entries",
//					new String[1]);
//
//			ArrayList<String> entries = new ArrayList<String>();
//
//			if (guestbookEntries[0] != null) {
//				entries = new ArrayList<String>(Arrays.asList(prefs.getValues(
//						"guestbook-entries", new String[1])));
//			}
//
//			String userName = ParamUtil.getString(request, "name");
//			String message = ParamUtil.getString(request, "message");
//			String entry = userName + "^" + message;
//
//			entries.add(entry);
//
//			String[] array = entries.toArray(new String[entries.size()]);
//
//			prefs.setValues("guestbook-entries", array);
//
//			try {
//				prefs.store();
//			}
//			catch (IOException ex) {
//				Logger.getLogger(GuestbookPortlet.class.getName()).log(
//						Level.SEVERE, null, ex);
//			}
//			catch (ValidatorException ex) {
//				Logger.getLogger(GuestbookPortlet.class.getName()).log(
//						Level.SEVERE, null, ex);
//			}
//
//		}
//		catch (ReadOnlyException ex) {
//			Logger.getLogger(GuestbookPortlet.class.getName()).log(
//					Level.SEVERE, null, ex);
//		}
	}

	public void deleteEntry(ActionRequest request, ActionResponse response) throws PortalException {
		long entryId = ParamUtil.getLong(request, "entryId");
		long guestbookId = ParamUtil.getLong(request, "guestbookId");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				Entry.class.getName(), request);

		try {
			response.setRenderParameter(
					"guestbookId", Long.toString(guestbookId));
			System.out.println("response render: "+response.getRenderParameters());
			_entryLocalService.deleteEntry(entryId, serviceContext);

			SessionMessages.add(request, "entryDeleted");
		}

		catch (Exception e) {
			Logger.getLogger(GuestbookPortlet.class.getName()).log(
					Level.SEVERE, null, e);

			SessionErrors.add(request, e.getClass().getName());

		}
	}

//	public void exportData(ActionRequest request,ActionResponse response)
//	{
//		System.out.println("Hello Export");
//		ThemeDisplay themeDisplay  =(ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
//		ServiceContext serviceContext = null;
//		try {
//			serviceContext = ServiceContextFactory.getInstance(
//					Entry.class.getName(), request);
//
//			Map<String, Serializable> taskContextMap = new HashMap<>();
//			taskContextMap.put("companyId", themeDisplay.getCompanyId());
//			taskContextMap.put("language", themeDisplay.getLanguageId());
//			taskContextMap.put("groupId", themeDisplay.getScopeGroupId());
//			System.out.println("asas");
//			BackgroundTask backgroundTask = BackgroundTaskManagerUtil.addBackgroundTask(themeDisplay.getUserId(),
//					themeDisplay.getScopeGroupId(), GuestbookEntryBackgroundTaskExecutor.class.getName(), GuestbookEntryBackgroundTaskExecutor.class.getName(), taskContextMap, serviceContext);
//
//			request.setAttribute("backgroundTaskId",
//					backgroundTask.getBackgroundTaskId());
//		}
//		catch (Exception e)
//		{
//			throw new RuntimeException(e);
//		}
//	}
}