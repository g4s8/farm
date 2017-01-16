/**
 * Copyright (c) 2016 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.crews.slack;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.zerocracy.crews.Question;
import com.zerocracy.jstk.Farm;
import com.zerocracy.pmo.Pmo;
import com.zerocracy.stk.StkByRoles;
import com.zerocracy.stk.StkSafe;
import com.zerocracy.stk.pmo.StkParent;
import java.io.IOException;
import java.util.Arrays;

/**
 * Set parent.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.1
 */
final class ReParent implements Reaction<SlackMessagePosted> {

    @Override
    public boolean react(final Farm farm, final SlackMessagePosted event,
        final SlackSession session) throws IOException {
        farm.deploy(
            new StkSafe(
                new SkPerson(farm, event, session),
                new StkByRoles(
                    new SkProject(farm, event),
                    new SkPerson(farm, event, session),
                    Arrays.asList("PO"),
                    new StkParent(
                        new Pmo(farm),
                        new SkPerson(farm, event, session),
                        event.getChannel().getId(),
                        new Question(event.getMessageContent()).arg("pid")
                    )
                )
            )
        );
        return true;
    }
}