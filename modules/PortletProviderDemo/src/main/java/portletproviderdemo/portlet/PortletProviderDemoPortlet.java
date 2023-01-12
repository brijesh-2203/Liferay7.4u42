package portletproviderdemo.portlet;

import portletproviderdemo.constants.PortletProviderDemoPortletKeys;
import portletproviderdemo.constants.PortletProviderDemoWebKeys;

import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author root322
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=PortletProviderDemo",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + PortletProviderDemoPortletKeys.PORTLETPROVIDERDEMO,
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class PortletProviderDemoPortlet extends MVCPortlet {

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String portletId = PortalUtil.getPortletId(renderRequest);
		System.out.println("Portlet i d : "+portletId);

		String message = "PortletProviderDemo Add Portlet Provider";

		try {
			PortletPreferences preferences =
				PortletPreferencesFactoryUtil.getPortletSetup(
					renderRequest, portletId);

			String className = preferences.getValue(
				"className", null);

			long classPK = GetterUtil.getLong(
				preferences.getValue("classPK", null));

			if (Validator.isNotNull(className) && (classPK > 0)) {
				AssetEntry assetEntry = _assetEntryLocalService.getEntry(
					className, classPK);

				message = assetEntry.getTitle(themeDisplay.getLocale());
			}
		}
		catch (PortalException pe) {
		}

		renderRequest.setAttribute(
			PortletProviderDemoWebKeys.PORTLET_PROVIDER_MESSAGE, message);

		super.doView(renderRequest, renderResponse);
	}

	@Reference(unbind = "-")
	private volatile AssetEntryLocalService _assetEntryLocalService;

}