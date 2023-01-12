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

<%@ include file="/errors/init.jsp" %>

<%

// According to http://www.webmasterworld.com/forum91/3087.htm a semicolon in
// the URL for a meta-refresh tag does not work in IE 6.

// To work around this issue, we use a URL without a session id for meta-refresh
// and rely on the load event on the body element to properly rewrite the URL.

// However, if the original request was an AJAX request, sending a redirect is
// less than ideal. In this case we will simply print the error message.

ErrorData errorData = pageContext.getErrorData();

int code = errorData.getStatusCode();

String msg = String.valueOf(request.getAttribute(JavaConstants.JAVAX_SERVLET_ERROR_MESSAGE));
String uri = errorData.getRequestURI();

if (_log.isWarnEnabled()) {
	_log.warn("{code=\"" + code + "\", msg=\"" + msg + "\", uri=" + uri + "}", exception);
}

String dynamicIncludeKey = DynamicIncludeKeyUtil.getDynamicIncludeKey(request.getHeader("Accept"));
String xRequestWith = request.getHeader(HttpHeaders.X_REQUESTED_WITH);
%>

<c:choose>
	<c:when test="<%= GetterUtil.getBoolean(request.getAttribute(WebKeys.UNKNOWN_VIRTUAL_HOST)) %>">
		<liferay-ui:message key="unknown-virtual-hostname" />: <%= PortalUtil.getHost(request) %>
	</c:when>
	<c:when test="<%= !Validator.isBlank(dynamicIncludeKey) %>">
		<liferay-util:dynamic-include key="<%= dynamicIncludeKey %>" />
	</c:when>
	<c:when test="<%= !StringUtil.equalsIgnoreCase(HttpHeaders.XML_HTTP_REQUEST, xRequestWith) %>">
		<%@ page contentType="text/html; charset=UTF-8" %>

		<html>

			<%
			String redirect = null;

			LayoutSet layoutSet = (LayoutSet)request.getAttribute(WebKeys.VIRTUAL_HOST_LAYOUT_SET);

			if (layoutSet != null) {
				redirect = PortalUtil.getPathMain();
			}
			else {
				redirect = PortalUtil.getPortalURL(PortalUtil.getValidPortalDomain(PortalUtil.getDefaultCompanyId(), request.getServerName()), request.getServerPort(), request.isSecure()) + PortalUtil.getPathContext() + PortalUtil.getRelativeHomeURL(request);
			}

			if (!request.isRequestedSessionIdFromCookie()) {
				redirect = PortalUtil.getURLWithSessionId(redirect, session.getId());
			}
			%>

			<head>
				<title></title>

				<meta content="1; url=<%= HtmlUtil.escapeAttribute(redirect) %>" http-equiv="refresh" />
			</head>

			<body onload="javascript:location.replace('<%= HtmlUtil.escapeJS(redirect) %>');">

				<!--
				The numbers below are used to fill up space so that this works properly in IE.
				See http://support.microsoft.com/default.aspx?scid=kb;en-us;Q294807 for more
				information on why this is necessary.

				12345678901234567890123456789012345678901234567890123456789012345678901234567890
				12345678901234567890123456789012345678901234567890123456789012345678901234567890
				12345678901234567890123456789012345678901234567890123456789012345678901234567890
				-->
			</body>
		</html>
	</c:when>
	<c:otherwise>
		<%@ page contentType="text/html; charset=UTF-8" %>

		<html>
			<head>
				<title>Http Status <%= code %> - <%= LanguageUtil.get(request, "http-status-code[" + code + "]") %></title>
			</head>

			<body>
				<h1>Http Status <%= code %> - <%= LanguageUtil.get(request, "http-status-code[" + code + "]") %></h1>

				<p>
					<liferay-ui:message key="message" />: <%= HtmlUtil.escape(msg) %>
				</p>

				<p>
					<liferay-ui:message key="resource" />: <%= HtmlUtil.escape(uri) %>
				</p>
			</body>
		</html>
	</c:otherwise>
</c:choose>

<%!
private static final Log _log = LogFactoryUtil.getLog("portal_web.docroot.errors.code_jsp");
%>