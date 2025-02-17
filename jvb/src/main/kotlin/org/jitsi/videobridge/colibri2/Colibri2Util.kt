/*
 * Copyright @ 2022 - Present, 8x8 Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jitsi.videobridge.colibri2

import org.jitsi.xmpp.extensions.colibri.ColibriConferenceIQ
import org.jitsi.xmpp.extensions.colibri2.Colibri2Error
import org.jitsi.xmpp.util.createError
import org.jivesoftware.smack.packet.IQ
import org.jivesoftware.smack.packet.StanzaError

fun createConferenceAlreadyExistsError(iq: IQ, conferenceId: String, colibri2: Boolean) = createError(
    iq,
    // Jicofo's colibri1 impl requires a bad_request
    if (colibri2) StanzaError.Condition.conflict else StanzaError.Condition.bad_request,
    "Conference already exists for ID: $conferenceId",
    if (colibri2) Colibri2Error(Colibri2Error.Reason.CONFERENCE_ALREADY_EXISTS) else null
)

fun createConferenceNotFoundError(iq: IQ, conferenceId: String, colibri2: Boolean) = createError(
    iq,
    // Jicofo's colibri1 impl requires a bad_request
    if (colibri2) StanzaError.Condition.item_not_found else StanzaError.Condition.bad_request,
    "Conference not found for ID: $conferenceId",
    if (colibri2) Colibri2Error(Colibri2Error.Reason.CONFERENCE_NOT_FOUND) else null
)

/**
 * For colibri1 use the ColibriConferenceIQ helper to preserve the presence of StanzaError.Type.CANCEL
 */
fun createGracefulShutdownErrorResponse(iq: IQ, colibri2: Boolean): IQ = if (colibri2) createError(
    iq,
    StanzaError.Condition.service_unavailable,
    "In graceful shutdown",
    Colibri2Error(Colibri2Error.Reason.GRACEFUL_SHUTDOWN)
) else ColibriConferenceIQ.createGracefulShutdownErrorResponse(iq)
