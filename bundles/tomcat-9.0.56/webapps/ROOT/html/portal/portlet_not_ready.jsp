<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/html/portal/init.jsp" %>

<portlet:defineObjects />

<%
String portletTitle = HtmlUtil.escape(PortalUtil.getPortletTitle(renderResponse));

if (portletTitle == null) {
	portletTitle = LanguageUtil.get(request, "portlet");
}
%>

<div class="alert alert-danger">
	<liferay-ui:message arguments="<%= portletTitle %>" key="is-not-ready" translateArguments="<%= false %>" />
</div>