/**
 * Copyright (c) 2016-2018 Zerocracy
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
package com.zerocracy.tk;

import com.zerocracy.Farm;
import com.zerocracy.pm.staff.Roles;
import com.zerocracy.pmo.Catalog;
import com.zerocracy.pmo.People;
import com.zerocracy.pmo.Pmo;
import java.io.IOException;
import org.cactoos.map.MapEntry;
import org.cactoos.map.SolidMap;
import org.takes.Request;
import org.takes.facets.auth.Identity;
import org.takes.facets.auth.PsCookie;
import org.takes.rq.RqWithHeaders;
import org.takes.rq.RqWrap;

/**
 * Request with logged in user.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.20
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class RqWithUser extends RqWrap {

    /**
     * Ctor.
     * @param farm The farm
     * @param req The request
     * @throws IOException If fails
     */
    public RqWithUser(final Farm farm, final Request req) throws IOException {
        super(RqWithUser.make(farm, req));
    }

    /**
     * Make it.
     * @param farm The farm
     * @param req The request
     * @return The request
     * @throws IOException If fails
     */
    private static Request make(final Farm farm,
        final Request req) throws IOException {
        final Catalog catalog = new Catalog(new Pmo(farm)).bootstrap();
        final String pid = "C00000000";
        catalog.add(pid, String.format("2017/07/%s/", pid));
        catalog.link(pid, "github", "test/test");
        final Roles roles = new Roles(
            farm.find(String.format("@id='%s'", pid)).iterator().next()
        ).bootstrap();
        final String uid = "yegor256";
        roles.assign(uid, "PO");
        new People(new Pmo(farm)).bootstrap().invite(uid, "mentor");
        return new RqWithHeaders(
            req,
            String.format(
                "Cookie: %s=%s",
                PsCookie.class.getSimpleName(),
                new String(
                    new CcSecure(farm).encode(
                        new Identity.Simple(
                            String.format("urn:github:%s", uid),
                            new SolidMap<String, String>(
                                new MapEntry<>("login", uid)
                            )
                        )
                    )
                )
            )
        );
    }

}
