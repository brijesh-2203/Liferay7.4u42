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

<%
boolean invokingSession = false;

if (Objects.equals(session.getId(), MaintenanceUtil.getSessionId())) {
	invokingSession = true;
}
%>

<html>
	<head>
		<meta content="30; url=<%= PortalUtil.getPortalURL(request) %>" http-equiv="refresh" />
	</head>

	<body>
		<center>
			<table border="0" cellpadding="0" cellspacing="0" height="100%" width="700">
				<tr>
					<td align="center" valign="middle">
						<table border="0" cellpadding="1" cellspacing="0" width="100%">
							<tr>
								<td bgcolor="#FF0000">
									<table border="0" cellpadding="0" cellspacing="0" width="100%">
										<tr>
											<td bgcolor="#FFFFFF">
												<br />

												<table border="0" cellpadding="10" cellspacing="0" width="100%">
													<tr>
														<td align="center">
															The system is currently undergoing maintenance. Please try again later.
														</td>
													</tr>

													<c:if test="<%= invokingSession %>">
														<tr>
															<td>
																<%= MaintenanceUtil.getStatus() %>
															</td>
														</tr>
													</c:if>
												</table>

												<br />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</center>
	</body>
</html>