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

<%@ include file="/html/taglib/init.jsp" %>

<%
SearchContainer<?> searchContainer = (SearchContainer<?>)request.getAttribute("liferay-ui:search:searchContainer");

String id = (String)request.getAttribute("liferay-ui:search:id");

if (Validator.isNull(id) && (searchContainer != null)) {
	id = searchContainer.getId(request, namespace);

	id = id.concat("PageIterator");
}

String markupView = (String)request.getAttribute("liferay-ui:search:markupView");
String type = (String)request.getAttribute("liferay-ui:search:type");

PortletURL iteratorURL = searchContainer.getIteratorURL();

if (iteratorURL != null) {
	iteratorURL.setParameter("resetCur", Boolean.FALSE.toString());
}
%>

<c:if test="<%= searchContainer.getTotal() > 0 %>">
	<liferay-ui:page-iterator
		cur="<%= searchContainer.getCur() %>"
		curParam="<%= searchContainer.getCurParam() %>"
		delta="<%= searchContainer.getDelta() %>"
		deltaConfigurable="<%= searchContainer.isDeltaConfigurable() %>"
		deltaParam="<%= searchContainer.getDeltaParam() %>"
		forcePost="<%= searchContainer.isForcePost() %>"
		id="<%= id %>"
		markupView="<%= markupView %>"
		maxPages="<%= PropsValues.SEARCH_CONTAINER_PAGE_ITERATOR_MAX_PAGES %>"
		portletURL="<%= iteratorURL %>"
		total="<%= searchContainer.getTotal() %>"
		type="<%= type %>"
	/>
</c:if>