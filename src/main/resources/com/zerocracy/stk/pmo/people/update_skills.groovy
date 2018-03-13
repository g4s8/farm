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
package com.zerocracy.stk.pmo.people

import com.jcabi.github.Github
import com.jcabi.github.User
import com.jcabi.xml.XML
import com.zerocracy.Farm
import com.zerocracy.Project
import com.zerocracy.entry.ExtGithub
import com.zerocracy.farm.Assume
import com.zerocracy.pmo.People

import java.security.SecureRandom

def exec(Project pmo, XML xml) {
  new Assume(pmo, xml).isPmo()
  new Assume(pmo, xml).type('Ping hourly')
  /**
   * @todo #492:30min Let's implement this stakeholder. It will take
   *  a random user from the list, which has the oldest updated attribute,
   *  go to its GitHub account, fetch all repositories he owns and contributes
   *  to and fetch the most popular languages from them.
   *  GitHub provides that information.
   */
  Farm farm = binding.variables.farm
  People people = new People(farm).bootstrap()
  List<String> ids = people.iterate().toList()
  Random random = new SecureRandom()
  String login = ids[random.nextInt(ids.size())]
  Github github = new ExtGithub(farm).value()
  new User.Smart(github.users().get(login)).publicRepos()
  // WAITING https://github.com/jcabi/jcabi-github/issues/1361
}