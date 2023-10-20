package seedu.application.logic.parser;

import static seedu.application.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.application.logic.commands.CommandTestUtil.*;
import static seedu.application.logic.parser.CliSyntax.*;
import static seedu.application.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.application.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.application.testutil.TypicalJobs.CLEANER;

import org.junit.jupiter.api.Test;

import seedu.application.logic.Messages;
import seedu.application.logic.commands.AddCommand;
import seedu.application.model.job.Company;
import seedu.application.model.job.Job;
import seedu.application.model.job.Role;
import seedu.application.testutil.JobBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Job expectedJob = new JobBuilder(CLEANER).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + ROLE_DESC_CLEANER
                + COMPANY_DESC_CLEANER + DEADLINE_DESC_CLEANER + STATUS_DESC_CLEANER,
            new AddCommand(expectedJob));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedJobString = ROLE_DESC_CLEANER + COMPANY_DESC_CLEANER
            + DEADLINE_DESC_CLEANER + STATUS_DESC_CLEANER;

        // multiple roles
        assertParseFailure(parser, ROLE_DESC_CHEF + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // multiple companies
        assertParseFailure(parser, COMPANY_DESC_CHEF + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));

        // multiple deadlines
        assertParseFailure(parser, DEADLINE_DESC_CHEF + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEADLINE));

        // multiple status
        assertParseFailure(parser, STATUS_DESC_CHEF + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));

        // multiple fields repeated
        assertParseFailure(parser,
            validExpectedJobString + COMPANY_DESC_CHEF + ROLE_DESC_CHEF
                + DEADLINE_DESC_CLEANER + STATUS_DESC_CLEANER
                + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE, PREFIX_COMPANY, PREFIX_DEADLINE, PREFIX_STATUS));

        // invalid value followed by valid value

        // invalid role
        assertParseFailure(parser, INVALID_ROLE_DESC + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // invalid company
        assertParseFailure(parser, INVALID_COMPANY_DESC + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));

        // invalid company
        assertParseFailure(parser, INVALID_DEADLINE_DESC + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEADLINE));

        // invalid status
        assertParseFailure(parser, INVALID_STATUS_DESC + validExpectedJobString,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));

        // valid value followed by invalid value

        // invalid role
        assertParseFailure(parser, validExpectedJobString + INVALID_ROLE_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROLE));

        // invalid company
        assertParseFailure(parser, validExpectedJobString + INVALID_COMPANY_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_COMPANY));

        // invalid deadline
        assertParseFailure(parser, validExpectedJobString + INVALID_DEADLINE_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DEADLINE));

        // invalid status
        assertParseFailure(parser, validExpectedJobString + INVALID_STATUS_DESC,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing role prefix
        assertParseFailure(parser, VALID_ROLE_CLEANER + COMPANY_DESC_CLEANER, expectedMessage);

        // missing company prefix
        assertParseFailure(parser, ROLE_DESC_CLEANER + VALID_COMPANY_CLEANER, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_ROLE_CLEANER + VALID_COMPANY_CLEANER, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid role
        assertParseFailure(parser, INVALID_ROLE_DESC + COMPANY_DESC_CLEANER, Role.MESSAGE_CONSTRAINTS);

        // invalid company
        assertParseFailure(parser, ROLE_DESC_CLEANER + INVALID_COMPANY_DESC, Company.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_ROLE_DESC + INVALID_COMPANY_DESC, Role.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + ROLE_DESC_CLEANER + COMPANY_DESC_CLEANER,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
