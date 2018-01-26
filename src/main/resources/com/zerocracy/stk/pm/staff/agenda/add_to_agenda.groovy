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
package com.zerocracy.stk.pm.staff.agenda

import com.jcabi.xml.XML
import com.zerocracy.Project
import com.zerocracy.farm.Assume
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut
import com.zerocracy.pm.cost.Estimates
import com.zerocracy.pm.in.Orders
import com.zerocracy.pm.scope.Wbs
import com.zerocracy.pmo.Agenda

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Order was given')
  ClaimIn claim = new ClaimIn(xml)
  String job = claim.param('job')
  Orders orders = new Orders(project).bootstrap()
  String owner = orders.performer(job)
  String role = new Wbs(project).bootstrap().role(job)
  Agenda agenda = new Agenda(project, owner).bootstrap()
  agenda.add(job, role)
  Estimates estimates = new Estimates(project).bootstrap()
  if (estimates.exists(job)) {
    agenda.estimate(job, estimates.get(job))
  }
  new ClaimOut()
    .type('Agenda was updated')
    .param('cause', claim.cid())
    .param('login', owner)
    .postTo(project)
}
