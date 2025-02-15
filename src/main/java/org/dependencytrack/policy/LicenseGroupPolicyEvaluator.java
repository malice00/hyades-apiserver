/*
 * This file is part of Dependency-Track.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright (c) Steve Springett. All Rights Reserved.
 */
package org.dependencytrack.policy;

import alpine.common.logging.Logger;
import org.dependencytrack.model.Component;
import org.dependencytrack.model.License;
import org.dependencytrack.model.LicenseGroup;
import org.dependencytrack.model.Policy;
import org.dependencytrack.model.PolicyCondition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Evaluates if a components resolved license is in the license group defined by the policy.
 *
 * @author Steve Springett
 * @since 4.0.0
 */
public class LicenseGroupPolicyEvaluator extends AbstractPolicyEvaluator {

    private static final Logger LOGGER = Logger.getLogger(LicenseGroupPolicyEvaluator.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public PolicyCondition.Subject supportedSubject() {
        return PolicyCondition.Subject.LICENSE_GROUP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PolicyConditionViolation> evaluate(final Policy policy, final Component component) {
        List<PolicyCondition> policyConditions = super.extractSupportedConditions(policy);
        if (policyConditions.isEmpty()) {
            return Collections.emptyList();
        }
        final List<PolicyConditionViolation> violations = new ArrayList<>();
        final License license = component.getResolvedLicense();

        for (final PolicyCondition condition : policyConditions) {
            LOGGER.debug("Evaluating component (" + component.getUuid() + ") against policy condition (" + condition.getUuid() + ")");
            final LicenseGroup lg = qm.getObjectByUuid(LicenseGroup.class, condition.getValue());
            if (lg == null) {
                LOGGER.warn("The license group %s does not exist; Skipping evaluation of condition %s of policy %s"
                        .formatted(condition.getValue(), condition.getUuid(), policy.getName()));
                continue;
            }
            if (license == null) {
                if (condition.getOperator() == PolicyCondition.Operator.IS_NOT) {
                    violations.add(new PolicyConditionViolation(condition, component));
                }
            } else {
                final boolean containsLicense = qm.doesLicenseGroupContainLicense(lg, license);
                if ((condition.getOperator() == PolicyCondition.Operator.IS && containsLicense)
                        || (condition.getOperator() == PolicyCondition.Operator.IS_NOT && !containsLicense)) {
                    violations.add(new PolicyConditionViolation(condition, component));
                }
            }

        }
        return violations;
    }

}
