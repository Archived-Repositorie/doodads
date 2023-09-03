/*
 * Copyright (c) 2023. JustFoxx
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package doodads;

import static org.junit.jupiter.api.Assertions.*;

import io.github.justfoxx.doodads.Option;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class OptionTest {
    @Test
    public void optionSome_isSome_true() {
        Option.Some<String> some = Option.some("foo");
        assertTrue(some.isSome());
    }

    @Test
    public void optionNone_isNone_true() {
        Option.None<String> none = Option.none();
        assertTrue(none.isNone());
    }

    @Test
    public void option_from_isSome_true() {
        Option<String> option = Option.from("foo");
        assertTrue(option.isSome());
    }

    @Test
    public void option_from_isNone_true() {
        Option<String> option = Option.from(null);
        assertTrue(option.isNone());
    }

    @Test
    public void optionSome_getOrDefault_equals_true() {
        Option<String> some = Option.some("foo");
        assertEquals("foo", some.getOrDefault("foo"));
    }

    @Test
    public void optionNone_getOrDefault_equals_true() {
        Option<String> none = Option.none();
        assertEquals("bar", none.getOrDefault("bar"));
    }

    @Test
    public void optionSome_getOrThrow_throws() {
        Option<String> some = Option.some("foo");
        assertDoesNotThrow(() -> some.getOrThrow());
    }

    @Test
    public void optionNone_getOrThrow_throws() {
        Option<String> none = Option.none();
        assertThrows(NoSuchElementException.class, none::getOrThrow);
    }

    @Test
    public void optionSome_getOrThrow_customThrows() {
        Option<String> some = Option.some("foo");
        assertDoesNotThrow(() -> some.getOrThrow(Exception::new));
    }

    @Test
    public void optionNone_getOrThrow_customThrows() {
        Option<String> none = Option.none();
        assertThrows(Exception.class, () -> none.getOrThrow(Exception::new));
    }

    @Test
    public void optionSome_ifPresent_prints() {
        Option<String> some = Option.some("foo");
        some.ifPresent(System.out::println);
    }

    @Test
    public void optionNone_ifPresent_notPrints() {
        Option<String> none = Option.none();
        none.ifPresent(System.out::println);
    }

    @Test
    public void optionSome_ifPresentOrElse_prints() {
        Option<String> some = Option.some("foo");
        some.ifPresentOrElse(System.out::println, () -> {});
    }

    @Test
    public void optionNone_ifPresentOrElse_notPrints() {
        Option<String> none = Option.none();
        none.ifPresentOrElse(System.out::println, () -> {});
    }

    @Test
    public void optionSome_getType_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(Option.Type.SOME, some.getType());
    }

    @Test
    public void optionNone_getType_equals() {
        Option<String> none = Option.none();
        assertEquals(Option.Type.NONE, none.getType());
    }

    @Test
    public void optionSome_filter_equals() {
        Option<String> some = Option.some("foo");
        assertTrue(some.filter(s -> s.equals("foo")).isSome());
    }

    @Test
    public void optionSome_filter_notEquals() {
        Option<String> some = Option.some("foo");
        assertFalse(some.filter(s -> s.equals("bar")).isSome());
    }

    @Test
    public void optionNone_filter_notEquals() {
        Option<String> none = Option.none();
        assertFalse(none.filter(s -> s.equals("foo")).isSome());
    }

    @Test
    public void optionSome_map_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(some.map(s -> s + "bar"), Option.some("foobar"));
    }

    @Test
    public void optionNone_map_equals() {
        Option<String> none = Option.none();
        assertEquals(none.map(s -> s + "bar"), Option.none());
    }

    @Test
    public void optionSome_flatMap_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(some.flatMap(s -> Option.some(s + "bar")), Option.some("foobar"));
    }

    @Test
    public void optionNone_flatMap_equals() {
        Option<String> none = Option.none();
        assertEquals(none.flatMap(s -> Option.some(s + "bar")), Option.none());
    }

    @Test
    public void optionSome_or_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(some.or(() -> Option.some("bar")), Option.some("foo"));
    }

    @Test
    public void optionNone_or_equals() {
        Option<String> none = Option.none();
        assertEquals(none.or(() -> Option.some("bar")), Option.some("bar"));
    }

    @Test
    public void optionSome_toString_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(some.toString(), "Option.Some(foo)");
    }

    @Test
    public void optionNone_toString_equals() {
        Option<String> none = Option.none();
        assertEquals(none.toString(), "Option.None");
    }

    @Test
    public void optionSome_stream_equals() {
        Option<String> some = Option.some("foo");
        assertEquals(some.stream().toList(), List.of("foo"));
    }

    @Test
    public void optionNone_stream_equals() {
        Option<String> none = Option.none();
        assertEquals(none.stream().toList(), List.of());
    }
}
