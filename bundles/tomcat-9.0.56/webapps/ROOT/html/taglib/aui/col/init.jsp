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

@generated
--%>

<%@ include file="/html/taglib/taglib-init.jsp" %>

<%
java.lang.String cssClass = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:cssClass"));
java.lang.String id = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:id"));
java.lang.String lg = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:lg"));
java.lang.String md = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:md"));
java.lang.String sm = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:sm"));
int span = GetterUtil.getInteger(String.valueOf(request.getAttribute("aui:col:span")), 12);
int width = GetterUtil.getInteger(String.valueOf(request.getAttribute("aui:col:width")));
java.lang.String xs = GetterUtil.getString((java.lang.String)request.getAttribute("aui:col:xs"));
Map<String, Object> dynamicAttributes = (Map<String, Object>)request.getAttribute("aui:col:dynamicAttributes");
%>

<%@ include file="/html/taglib/aui/col/init-ext.jspf" %>