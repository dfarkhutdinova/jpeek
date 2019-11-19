/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jpeek.skeleton;

import com.jcabi.matchers.XhtmlMatchers;
import org.hamcrest.MatcherAssert;
import org.jpeek.FakeBase;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.xembly.Directives;
import org.xembly.Xembler;

/**
 * Test case for {@link XmlClass}.
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.27
 * @checkstyle JavadocMethodCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class XmlClassTest {

    @Test
    public void parsesClass() {
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(this.classAsXml("Bar")),
            XhtmlMatchers.hasXPaths(
                "/class/methods[count(method) = 5]",
                "/class/attributes[count(attribute) = 4]"
            )
        );
    }

    @Test
    public void parsesMethodVisibility() {
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                this.classAsXml("ClassWithDifferentMethodVisibilities")
            ),
            XhtmlMatchers.hasXPaths(
                "/class/methods/method[@visibility = 'public']",
                "/class/methods/method[@visibility = 'private']",
                "/class/methods/method[@visibility = 'default']",
                "/class/methods/method[@visibility = 'protected']"
            )
        );
    }

    @Test
    public void thereIsNoAttributePublic() {
        new Assertion<>(
            "attribute public does not exists",
            () -> XhtmlMatchers.xhtml(
                this.classAsXml("ClassWithDifferentMethodVisibilities")
            ),
            XhtmlMatchers.hasXPaths(
                "/class/methods/method[not (@public)]"
            )
        ).affirm();
    }

    // @todo #297:30min this method appears three times and may be useful for
    // future tests, so extract the method as a new class.
    private String classAsXml(final String name) {
        return new Xembler(
            new Directives().add("class").append(
                new XmlClass(
                    new Classes(
                        new FakeBase(name)
                    ).iterator().next()
                )
            )
        ).xmlQuietly();
    }
}
