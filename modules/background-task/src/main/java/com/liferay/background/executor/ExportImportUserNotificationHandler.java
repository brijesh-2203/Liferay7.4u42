package com.liferay.background.executor;

import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.background.task.model.BackgroundTask;
import com.liferay.portal.background.task.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplay;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplayFactory;
import com.liferay.portal.kernel.backgroundtask.display.BackgroundTaskDisplayFactoryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.BaseUserNotificationHandler;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringUtil;
import org.osgi.service.component.annotations.Component;

import javax.portlet.PortletRequest;
import java.util.Locale;

@Component(
        immediate = true,
        property = {"javax.portlet.name=" + "BackgroundTaskPortlet"},
        service = UserNotificationHandler.class
)
public class ExportImportUserNotificationHandler extends BaseUserNotificationHandler {


    public ExportImportUserNotificationHandler() {
        setOpenDialog(true);
        setPortletId("BackgroundTaskPortlet");
    }

    @Override
    protected String getBody(UserNotificationEvent userNotificationEvent, ServiceContext serviceContext) throws Exception {

        Locale locale = PortalUtil.getLocale(serviceContext.getRequest());

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
                userNotificationEvent.getPayload());

        String notificationTitle=jsonObject.getString("backgroundTaskId");
        String notificationDetails = jsonObject.getString("status");
        String body = StringUtil.replace(getBodyTemplate(),
                new String[] {"[$TITLE$]", "[$BODY_TEXT$]" },
                new String[] {"<strong>" + notificationTitle + "</strong>", notificationDetails });
        String message = "Message is this";

//        long backgroundTaskId = jsonObject.getLong("backgroundTaskId");
//        System.out.println("payload : "+backgroundTaskId);
//        BackgroundTaskDisplay backgroundTaskDisplay =
//                BackgroundTaskDisplayFactoryUtil.getBackgroundTaskDisplay(
//                        backgroundTaskId);
//        String processName = backgroundTaskDisplay.getDisplayName(
//                serviceContext.getRequest());
        System.out.println("aksjkajsajskajska");
//        return LanguageUtil.format(locale, message, processName);
        return body;
    }
    @Override
    protected String getBodyTemplate() throws Exception {
        return "<div class=\"title\">[$TITLE$]</div><div>[$BODY_TEXT$]</div>";
    }
    @Override
    protected String getLink(UserNotificationEvent userNotificationEvent, ServiceContext serviceContext) throws Exception {
        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
                userNotificationEvent.getPayload());

        long backgroundTaskId = jsonObject.getLong("backgroundTaskId");

        BackgroundTask backgroundTask =
                BackgroundTaskLocalServiceUtil.fetchBackgroundTask(backgroundTaskId);

        if (backgroundTask == null) {
            return StringPool.BLANK;
        }

        return PortletURLBuilder.create(
                PortletURLFactoryUtil.create(
                        serviceContext.getRequest(),
                        "BackgroundTaskPortlet",
                        PortletRequest.RENDER_PHASE)
        ).setMVCPath(
                "/view_export_import.jsp"
        ).setBackURL(
                serviceContext.getCurrentURL()
        ).setParameter(
                "backgroundTaskId", backgroundTaskId
        ).buildString();
    }
}
