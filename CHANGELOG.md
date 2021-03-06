# Change Log

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

[Unreleased]: https://github.com/atomist/rug/compare/1.0.0-m.5...HEAD

### Added

-   Parameter and MappedParameter decorated properties from super classes are now inherited
    by sub-classes [#646][646]

-   Added `GITHUB_DEFAULT_REPO_VISIBILITY` mapped parameter that determines
    repository visibility based on organization GitHub plan

### Changed

-   Updated typescript & typedoc used for doc generation to 2.4.1 and 0.7.1 respectively
    
-   Added `post` property to `UpdatableMessage` to indicate if we message should be written if it
    didn't previously existed
    
### Fixed

-   Ensure default values for optional parameters to Rug Functions
    are used, instead of null. Primitives are converted to 0 or false

[646]: https://github.com/atomist/rug/issues/646

## [1.0.0-m.5] - 2017-07-11

[1.0.0-m.5]: https://github.com/atomist/rug/compare/1.0.0-m.4...1.0.0-m.5

Milestone 5 (Summer) release

### Added

-   Added new `UpdatableMessage` to allow messages to be re-written in
    the bot

### Changed

-   **BREAKING** Removed all usage of Scala `Future` in `PlanRunner`
    and corresponding APIs
-   Updated JavaParser to 3.2.8
-   Updated artifact-source to 1.0.0-m.5

### Fixed

-   Ensure index and original array are also passed to map callback
    functions [#632][632]
-   Now only load Gherkin feature definitions from the `.atomist/tests`
    directory [#629][629]

[632]: https://github.com/atomist/rug/issues/632
[629]: https://github.com/atomist/rug/issues/629

## [1.0.0-m.4] - 2017-06-01

[1.0.0-m.4]: https://github.com/atomist/rug/compare/1.0.0-m.3...1.0.0-m.4

Milestone 4 (whiskey) release

### Added

-   Add internal support for `Scope` on Rugs. Currently this is only used
    on Response Handlers, and will be used to change the visibility of 
    auto-generated ones so they are not indexed, displayed etc by the CLI
    or Bot.
-   Add support for @IntegrationTest decorator. TypeScript Classes with
    this decorator will behave much like Command Handlers, except in that
    they will have additional metadata allowing the CLI or runtime to behave
    differently when required.
-   Exposed underlying TypeScript class decorator logic so that they can
    be called directly to emit Rugs programmatically [#595][595]
-   Allow `commitMessage` to be set on `Edit` instructions [#593][593]
-   Ability to implement the `startingPoint` method in a generator to
    start with an empty repo or the content of a Git repo, if the runtime permits.
-   Added `HandlerContext` as second parameter to `ResponseHandler.handle`
    invocations [#587][587]
-   Added `Identifiable` instruction that can be used for `DirectedMessage`
    and `ResponseMessage`
-   Rug Language Extensions can provide a preprocessing (and postprocessing) step.
    This lets them do string manipulation before parsing, and undo it afterward.

### Changed

-   **BREAKING** Removed support for Velocity templates from `Project`
-   Allow `editWith` and `generateWith` calls in Gherkin tests without parameters
-   Parameters passed to ResponseHandlers with no declared parameters are now
    set directly on the instance. This can be used to avoid using @Parameter
    declarations
-   Handle project generate failures
-   **BREAKING** remove ProjectEditor & ProjectGenerator interfaces.
    This also removes support for legacy parameter[] Parameter declarations
-   **BREAKING** remove tags from Parameters (they are unused)
-   Remove some more remnants of reviewers
-   Harmonized much of the Rug loading code between project operations and
    handlers
-   RugLanguageExtensions may return a smaller interface, `ParsedNode` from 
    their parsing method `fileToRawNode`, instead of requiring `PositionedTreeNode`
-   Removed CommonViewOperations with its deprecated `fail` and `println` methods
-   Removed unnecessary InstantEditorFailureException as TypeScript can throw errors
    
### Fixed

-   Decorators now hide defined properties by setting `enumerable=false` [#554][554]
-   Fixed handling of fields set with Object.defineProperty (now enumerable)
-   @Editor, @Generator, and @EventHandler decorators now allow 'name' to be
    omitted. If you omit the name, it uses the name of the class.
-   `files` in ProjectView now returns a list of FileMutableView [#623][623]


[593]: https://github.com/atomist/rug/issues/593
[554]: https://github.com/atomist/rug/issues/554
[595]: https://github.com/atomist/rug/issues/595
[587]: https://github.com/atomist/rug/issues/587
[623]: https://github.com/atomist/rug/issues/623

## [1.0.0-m.3] - 2017-05-09

[1.0.0-m.3]: https://github.com/atomist/rug/compare/1.0.0-m.2...1.0.0-m.3

May milestone 3 release

### Added

-   `pathExpressionEngine` and `contextRoot` now available within event handlers
-   **BREAKING** Added ability to specific `target` of an Edit Instruction
    so that source/target branches, title and body can be specified. Breaks
    the ProjectManagement Scala interface only.
-   Added ability to indicate in TypeScript generated interfaces when a
    JVM method being exposed is marked as `@Deprecated`
-   Ability to test when a scenario aborts [#569][569]
-   Types to Gherkin well-known step callback signatures [#574][574]

### Changed

-   Standardize use of `Object.defineProperty` in TypeScript decorators
-   If the first parameter to a Rug Kind TypeScript decorator is
    omitted, the constructor's name is used instead. E.g. it is now
    possible to declare a command handler with:
    `@CommandHandler("description only")`
-   **BREAKING** Gherkin handler step 'parameters were invalid'
    renamed to 'handler parameters were invalid' [#566][566]
-   **BREAKING** Removed Spring and Spring project types, these don't
    belong in `rug` and will resurface in a separate project
-   Add instructions to DirectedMessage and ResponseMessage
-   Add optional id to Presentable instruction
-   Missing implementations of Given and When steps now are returned
    as NotYetImplemented in testing results [#570][570]

### Fixed

-   Improve archive loading performance by creating new ArtifactSource
    with only Rug content, instead of filtering all files in original
    archive
-   Throw exceptions if there is more than one Rug with the same name
    in an archive [#561][561]
-   Fix StackOverflow when calling toString on
    LinkableContainerTreeNode, can't recurse as this is a graph not a
    tree.
-   Remove node_modules from TypeScript sources path [#442][442]

[569]: https://github.com/atomist/rug/issues/569
[566]: https://github.com/atomist/rug/issues/566
[561]: https://github.com/atomist/rug/issues/561
[570]: https://github.com/atomist/rug/issues/570
[574]: https://github.com/atomist/rug/issues/574
[442]: https://github.com/atomist/rug/issues/442

## [1.0.0-m.2] - 2017-04-26

[1.0.0-m.2]: https://github.com/atomist/rug/compare/1.0.0-m.1...1.0.0-m.2

Initial milestone 2 release

### Changed

-   Fix NPM publish for milestone and release candidate releases
-   Consolidate NPM publish for dev and release versions

## [1.0.0-m.1] - 2017-04-26

[1.0.0-m.1]: https://github.com/atomist/rug/compare/0.26.1...1.0.0-m.1

Initial milestone 1 release

### Added

-   Support for cloning a repo from GitHub in Gherkin tests, via
    `ScenarioWorld`

### Changed

-   **BREAKING** TypeScript `Match` now uses property access, not function
-   Updated all Atomist dependencies to 1.0.0-m.1 versions
-   Handlers are now picked up from arrays as well as regular `vars`

### Fixed

-   `project.findFile` now works on paths starting with a
    '/' [#523][523]
-   Rug Function exceptions are now passed to error handlers.  Maybe:
    Response now passed to handlers when unknown Rug Functions are
    encountered [#536][536]

[523]: https://github.com/atomist/rug/issues/523
[536]: https://github.com/atomist/rug/issues/536

## [0.26.1] - 2017-04-24

[0.26.1]: https://github.com/atomist/rug/compare/0.26.0...0.26.1

Secret release

### Added

-  Added support for resolution of Secrets from Event Handlers. NOTE:
   it's up to secret resolver implementations to make sense of what
   this means in the particular context!

### Changed

-  Improved JVM `toString` behavior for JavaScript proxies
-  Added reducer to allow JSON.stringify to be used with JVM-backed nodes

## [0.26.0] - 2017-04-21

[0.26.0]: https://github.com/atomist/rug/compare/0.25.3...0.26.0

Election Release

### Added

-   Added ability to resolve `Project` objects from `Repo` or `Commit`, if a `RepoResolver` is provided
    by the runtime
-   Added `Impact` object to enable drilling into the changes in a `Push` in a path expression, extracting
    old and new files.
-   Added `stringify` function on `TreeHelper.ts` to help dump hierarchy of AST nodes when debugging.
-   `jsSafeCommittingProxy` now returns `undefined` on unknown properties on dynamic nodes
    rather than throwing an exceptions

### Changed

-   Response.Status of instructions whose failure is handled by a ResponseHandler
    now have a status of `Handled` instead of `Failed`
-   **BREAKING** Event and Command Handlers must return EventPlan and CommandPlan
    respectively to remove confusion as to the different capabilities
-   Project test steps can now return `void`, as for handlers.

## [0.25.3] - 2017-04-13

[0.25.3]: https://github.com/atomist/rug/compare/0.25.2...0.25.3

Parameter release

### Changed

-   Parameter decorator now takes a copy of the parameter configuration.
    It _was_ overwriting its contents.

## [0.25.2] - 2017-04-13

[0.25.2]: https://github.com/atomist/rug/compare/0.25.1...0.25.2

Signature release

### Added

-   Add GPG signing of published artifacts

### Changed

-   Fixes [#517][501]: Converting script objects returned from tests to GraphNode

-   Better error message when LifecycleMessage does not contain a GraphNode

[501]: https://github.com/atomist/rug/issues/501

## [0.25.1] - 2017-04-11

[0.25.1]: https://github.com/atomist/rug/compare/0.25.0...0.25.1

Last minute release

### Changed

-   msg, body, code and status are now properties of TS Response

## [0.25.0] - 2017-04-11

[0.25.0]: https://github.com/atomist/rug/compare/0.24.0...0.25.0

Judgement Day Release

### Changed

-   **BREAKING** Generated TypeScript project model types use
    properties rather than functions for no-arg operations marked with
    `@ExportFunction` when the new `exposeAsProperty` flag is true
-   **BREAKING** All Generated TypeScript Cortex types use properties
    rather than functions for all navigation
-   **BREAKING** Use specific message types in Plans for the different
    scenarios (directed, lifecycle and response) as per [#501][501]
-   Improved build
-   Make TypeScript generator for rug more like that for cortex
-   Improve release documentation

[501]: https://github.com/atomist/rug/issues/501

## Added

-   The `@atomist/rug` npm module snapshots are now published to
    https://atomist.jfrog.io/atomist/api/npm/npm-dev

### Removed

-   **BREAKING** The Stubs.ts file is no longer generated, use the
    Types.ts instead

## [0.24.0] - 2017-04-04

[0.24.0]: https://github.com/atomist/rug/compare/0.23.0...0.24.0

The one TS to rule all stubs

### Changed

-   Model stubs are generated in one uber file
-   Generally refined TS generation

### Added

-   Set the channel id on a message with `withChannelId`

## [0.23.0] - 2017-03-31

[0.23.0]: https://github.com/atomist/rug/compare/0.22.0...0.23.0

TypeScript stub generation and proxy improvement release

### Changed

-   Now generating `add` instead of `with` methods to handle arrays in
    TypeScript code generation

## [0.22.0] - 2017-03-31


### Changed

-   **BREAKING** internal API for resolving and loading Rugs has changed to deal with
    more complex dependency resolution scenarios. Also removal of AddressableRug and
    RugSupport traits.

[0.22.0]: https://github.com/atomist/rug/compare/0.21.0...0.22.0

Back from the future release

### Changed

-   Removed use of `Futures` in `LocalPlanRunner`
-   Changed `ServiceLoader`-based registries to not use caching
-  If the fixture is the same as the world in a Gherkin step, pass only one parameter. This changes the
signatures of handler tests.

## [0.21.0] - 2017-03-30

[0.21.0]: https://github.com/atomist/rug/compare/0.20.0...0.21.0

Handler return release

### Changed

-  **BREAKING** Handlers must return `Plan`, not `Message`
-  **BREAKING** Messages can only contain a string value, not JSON
-  Cardinality is processed in `TreeNode` deserialization from Cortex ingestion

## [0.20.0] - 2017-03-29

[0.20.0]: https://github.com/atomist/rug/compare/0.19.0...0.20.0

Handler testing improvement release

### Added

-   Added defaultValue parameter to @Parameter annotation

### Changed

-   Publishing events on path expression evaluations in handler tests
-   Now pass back JavaScript Plan to Gherkin steps
-   Can now record more than one plan in Gherkin handler tests. Fixes #480
-   TypeScript Plan class now exposes properties rather than functions
-   Do not include TypeDoc in NPM module

## [0.19.0] - 2017-03-28

[0.19.0]: https://github.com/atomist/rug/compare/0.18.3...0.19.0

### Added

-   Added tag to commit back relationship
-   Added platform property on builds
-   Added new methods for pluginManagement

### Changed

-   Better error message when failing to navigate property
-   Better error message when handler was not registered in a test
-   Building typedocs for cortex as well as rug

## [0.18.3] - 2017-03-27

[0.18.3]: https://github.com/atomist/rug/compare/0.18.2...0.18.3

Minor path expression fix release

## [0.18.2] - 2017-03-27

[0.18.2]: https://github.com/atomist/rug/compare/0.18.1...0.18.2

Re-Re-Cortex release

### Fixed

-   NPM publish of cortex

## [0.18.1] - 2017-03-27

[0.18.1]: https://github.com/atomist/rug/compare/0.18.0...0.18.1

Re-Cortex release

### Fixed

-   Cortex package.json

### Added

-   Generate and publish cortex TypeDoc

## [0.18.0] - 2017-03-27

[0.18.0]: https://github.com/atomist/rug/compare/0.17.3...0.18.0

Cortex release

### Added

-   Add placeholder npm module @atomist/native for pure
    TypeScript/JavaScript helpers that have no reliance on JVM objects
    (to be split out later)
-   Support for generation of TypeScript interfaces and stub classes
    for Atomist "cortex" model around commits, builds etc. Lives in
    @atomist/cortex module.

### Changed

-   Travis CI build always uses npm-release Maven profile

## [0.17.3] - 2017-03-24

[0.17.3]: https://github.com/atomist/rug/compare/0.17.2...0.17.3

Fixup before prod rollout release

### Fixed

-   'null' and undefined are not serialized to JSON in instruction
    parameters [#466][466]
-   NodesWithTag allows dynamic nodes on `Child` axis to not match
    without error
-   Don't set default parameter values if they are null [#458][458]
-   Support optional parameters in AnnotatedRugFunctions [#462][462]

[466]: https://github.com/atomist/rug/issues/466
[458]: https://github.com/atomist/rug/issues/458
[462]: https://github.com/atomist/rug/issues/462

## [0.17.2] - 2017-03-22

[0.17.2]: https://github.com/atomist/rug/compare/0.17.1...0.17.2

Quiet release

### Added

-   Allow type safe creation of plan instructions using instantiated operations
-   Allow construction of Message without body

### Fixed

-   Ensure Event Handlers are not invoked if there are no PE matches
    https://github.com/atomist/rug/issues/454

### Added

-   A message method on `HandlerScenarioWorld` when an handler only returns a
    message without a plan

## [0.17.1] - 2017-03-20

[0.17.1]: https://github.com/atomist/rug/compare/0.17.0...0.17.1

Relaxed release

### Changed

-   Removed validation of secret paths as this contract is owned by the bot

## [0.17.0] - 2017-03-20

[0.17.0]: https://github.com/atomist/rug/compare/0.16.0...0.17.0

Handler cleanup release

### Added

-   Added support for parameterized Gherkin steps, per #431

### Changed

-   **BREAKING** MappedParameter names changed to be more specific
-   **BREAKING** changed `withTreeNode` to `withNode` in Message class in Handlers.ts

## [0.16.0] - 2017-03-19

[0.16.0]: https://github.com/atomist/rug/compare/0.15.1...0.16.0

Test good parameters release

### Added

-   Testing well-known step "parameter were valid"

## [0.15.1] - 2017-03-19

[0.15.1]: https://github.com/atomist/rug/compare/0.15.0...0.15.1

Test bad parameters release

### Fixed

-   Testing editors no longer crashes when provided invalid
    parameters [#444][444]

[444]: https://github.com/atomist/rug/issues/444

### Added

-   Query by example option for authoring path expressions against model

## [0.15.0] - 2017-03-17

[0.15.0]: https://github.com/atomist/rug/compare/0.14.0...0.15.0

Early often release

### Added

-   Next phase of handler testing support
-   Adds the concept of a contextRoot node to RugContext to enable
    handlers to run path expressions as needed.  This change impacts
    runtime as well as testing, but is necessary to support our
    programming model.
-   Gherkin testing "the archive root" Given
-   TypeScript test documentation to generated TypeDoc

### Changed

-   **BREAKING** Removed `TreeHelper` class. This module now contains only
    functions, as a class was not necessary.
-   **BREAKING** Python Requirements old type has been removed as it
    was not matching the current programming model.  It will be moved
    to its own project.  [#434][434]
-   Improve duplicate removal in expression engine
-   Make TypeScript test stubs addressable (to help with deduping)
-   Correct some usages of TreeNode where GraphNode could be used
-   **BREAKING** The `generateWith` method used in testing has an
    additional `projectName` parameter between the generator and its
    parameters.

[434]: https://github.com/atomist/rug/pull/434

### Fixed

-   Editor test project objects have a name [#436][436]

[436]: https://github.com/atomist/rug/issues/436

## [0.14.0] - 2017-03-15

[0.14.0]: https://github.com/atomist/rug/compare/0.13.0...0.14.0

Ides of March release

### Changed

-   TypeDoc, ScalaDoc, and scoverage reports are now published
    automatically
-   Xml extension can now derive itself from a file
-   `RugContext` now exposes a `contextRoot` to allow handlers to
     execute arbitrary path expressions
-   Generators should now be under `.atomist/generators` but are still
    found under `.atomist/editors`

### Added

-   Command and event handler testing
-   TypeScript "it should fail" test step via @justinedelson #429
-   Xml `addNodeIfNotPresent` via @justinedelson #423

### Fixed

-   TypeScript interface and class generation #427

## [0.13.0] - 2017-03-10

[0.13.0]: https://github.com/atomist/rug/compare/0.12.0...0.13.0

Unlucky release

### Added

-   Annotations and helper class for implementing RugFunctions
-   New BDD test framework for Rug archives, based on Gherkin and
    TypeScript/JavaScript
-   Support for replacing tokens with secrets in RugFunction parameters
-   Support for resolving secrets during Plan execution
-   Support for MappedParameters to pull config from other systems
-   Support for Secrets in RugCommands
-   Support for Command, Event and Response Handlers. All return Plans
-   Support for RugFunctions - parameterized JVM functions for Handlers
-   Directory extension now public #409

### Fixed

-   Referencing of editors in other archives in JS archives
-   Bug in LinkedJsonTreeDeserializer where it threw NPE on empty
    result set from neo4j-ingester e.g. where the PE didn't match. Now
    returns EmptyLinkableContainerTreeNode

### Changed

-   Upgraded TypeScript to 2.2.1 via rug-typescript-compiler 0.12.0
-   **BREAKING** Removed Rug DSL-based BDD support. Use new Gherkin-based support.
-   **BREAKING** Removed rug namespace support in preference to common
    approach of group:artifact:name This means that DSL based rugs no
    longer contain their namepsace in their name
-   **BREAKING** Only require context when running handlers
-   **BREAKING** Removed CommandRegistry and related usages
-   **BREAKING** Renamed old RugFunction to RugDslFunction
-   New text node structure. See [docs/treenodes.md]().
-   **BREAKING** Removed ProjectOperationArguments. Use ParameterizedRug instead
-   **BREAKING** Refactored loading of Rugs from archives. Now returns `Rugs`
-   **BREAKING** Removed Executor and old Handlers completely

## [0.12.0] - 2017-02-14

[0.12.0]: https://github.com/atomist/rug/compare/0.11.0...0.12.0

Valentine release

### Added

-   Added YamlType and YamlFileType classes to replace YmlType and
    YmlFileType respectively. Deprecated the latter classes.
-   Support PathExpressions that start with a NodesWithTag
-   Support for `contains` and `starts-with` path expression functions
-   Beginning of a TypeScript class library to simplify working with
    tree nodes and transparently decorate tree nodes if a decoration
    strategy is provided in TypeScript.
-   Add new minimal TS interfaces for use with class
    decorators. e.g. EditProject
-   Added support for @Editor, @Generator, @Reviewer and @Tags class
    decorators in TS.
-   Added `ScalaFileType` backed by ScalaMeta

### Fixed

-   Elm parser failed on files with two multiline comments [#268][268]
-   Raise an `InvalidRugTestScenarioName` when a Rug test scenario is
    missing a name #71
-   Implicit DLS parameters (from `uses`) are no longer duplicate if
    multiple editors declare the same parameter. The first one is
    chosen [#258][258]
-   Ensure TS parameters are required by default, and ensure defaults
    are applied before validation [#224][224]
-   Changed how descendants were found and processed in tree
    expressions which fixed two pendingUntilFixed tests

[268]: https://github.com/atomist/rug/issues/268
[258]: https://github.com/atomist/rug/issues/258
[224]: https://github.com/atomist/rug/issues/224

### Changed

-   Upgrade TS compiler to 2.1.5
-   `scalar` method on TypeScript `PathExpressionEngine` taking string
    is now called `scalarStr`. This change was necessary to avoid
    conflicts with method overloading in TypeScript/JavaScript.
-   `TreeNodeUtils.toShorterString` now optionally takes a function
    to customize the display of each node.
-   **BREAKING** `value`, `update` and `formatInfo` functions from
    TypeScript `TreeNode` moved to new `TextTreeNode` sub-interface.
-   **BREAKING** The interface for Rug extensions, formerly Rug types,
    was simplified.  resolvesFromNodeTypes was eliminated and the
    signature for findAllIn was simplified.  They should also directly
    extend ChildResolver instead of some sort of ViewFinder.

## [0.11.0] - 2017-02-01

[0.11.0]: https://github.com/atomist/rug/compare/0.10.0...0.11.0

### Added

-   Implement JsonBackedContainerTreeNode, allows a ContainerTreeNode to
    maintain the Json is was generated from
-   Add correlationId to Message, allows handler to define
    how Messages are correlated
-   Support for @parameter TS class field decorators as
    per [#229][229]
-   Support for a new TS (JS) Handler programming model as
    per [#105][105]
-   Support for Type extensions/TreeNode written in TypeScript as
    per [#214][214]
-   Generators are now declared with the `generator` keyword
-   Optional predicates in tree expressions

[229]: https://github.com/atomist/rug/issues/229
[105]: https://github.com/atomist/rug/issues/105
[214]: https://github.com/atomist/rug/issues/214

### Fixed

-   LinkedJsonTreeDeserializer now properly returns string values
-   LinkedJsonTreeDeserializer now works when an already linked object
    is updated
-   TS generators are now passed project name as second argument as
    per TS contract
-   Retain all changes from an editor [#199][199]
-   Yml type can now be instantiated from ProjectMutableView,
    DirectoryMutableView, and FileMutableView [#250][250]
-   Handle YAML files with multiple documents, but only first is
    parsed and addressable.

[199]: https://github.com/atomist/rug/issues/199
[250]: https://github.com/atomist/rug/issues/250

### Changed

-   **BREAKING** `TreeNode.nodeType` renamed to `TreeNode.nodeTags`
-   We now create a new JS rug for each thread for safety [#78][78]
-   **BREAKING** all JS based Rugs must export (a la Common-JS) vars implementing
    the associated interfaces. Previously we scanned for all top level vars.
-   **BREAKING** Remove Executor support from Rug DSL as per [#206][206]
-   TypeScript editors now return void. Use the new
    `ProjectMutableView` `describeChange` method to add any comments
    about the working of your editor.
-   **BREAKING** CustomizingProjectGenerator was removed from
    ProjectGenerator.ts as it's no longer required, and it's thought
    that it's not being used at all yet.
-   **BREAKING** signature of TypeScript ProjectGenerator.populate()
    changed: parameter `projectName` got removed. Name of the
    generated project can be obtained via `project.name()`.
-   Core.ts is generated and compiled on-the-fly during unit-testing
    so that the build is not dependent on network or later maven
    phases until deployment
-   Scala compiler warnings are now fatal by default, use `-P
    allow-warnings` profile to have the old behavior.  The Travis CI
    build uses the allow-warnings profile.

[78]: https://github.com/atomist/rug/issues/78
[206]: https://github.com/atomist/rug/issues/206

### Deprecated

-   The `@generator` has been deprecated in favor of the `generator`
    keyword

## [0.10.0] - 2017-01-18

[0.10.0]: https://github.com/atomist/rug/compare/0.9.0...0.10.0

### Added

-   Parameter validation regex `@version_range`
-   ScriptExceptions during initial eval of JavaScript now include
    filename
-   EveryPom type curtesy of @justinedelson

### Changed

-   TypeScript/JavaScript should now "export" instances of Rugs inline
    with CommonJS standard.  Hopefully not a breaking change as there
    is limited support for automatically exporting legacy ones.

### Fixed

-   Comments are removed from JS files as they are 'required', and
    relative imports now work correctly when 'export' is used from TS
    or vars are added to the global exports var [#156][156]
-   Generation of Rug types documentation
-   minLength and maxLength now default to -1 as per Rug
    DSL [#169][169]
-   Allow Java annotations with properties [#164][164]
-   Default parameter values are now validated [#168][168]
-   Output parameter name when pattern fails to validate [#58][58]

[156]: https://github.com/atomist/rug/issues/156
[169]: https://github.com/atomist/rug/issues/169
[164]: https://github.com/atomist/rug/issues/164
[168]: https://github.com/atomist/rug/issues/168
[58]: https://github.com/atomist/rug/issues/58

### Removed

-   In the DSL, parameters no longer accept an array of allowed
    values for validation.

## [0.9.0] - 2017-01-09

[0.9.0]: https://github.com/atomist/rug/compare/0.8.0...0.9.0

TypeScripting release

### Added

-   TypeScript Reviewers

### Fixed

-   TS parameter tags were not being extracted [#151][151]
-   Parameters for TS editors/generators were defaulting to
    displayable=false.  They now default to
    displayable=true.  [#148][148]

[151]: https://github.com/atomist/rug/issues/151
[148]: https://github.com/atomist/rug/issues/148

## [0.8.0] - 2017-01-04

[0.8.0]: https://github.com/atomist/rug/compare/0.7.1...0.8.0

Breaking release

### Added

-   NavigationAxis: New AxisSpecifier that allows navigating via a
    property in path expression language
-   Path expression predicates can contain path expressions

### Changed

-   **BREAKING** Naming convention for Rug types now UpperCamelCase,
    basically the same as the Scala class/type without the trailing
    Type, MutableView, or TreeNode
-   **BREAKING** Rug tree expression syntax has been changed to more
    closely resemble XPath where possible
-   **BREAKING** Rug DSL: A Rug can no longer specify its action in JavaScript:
    In this case, use TypeScript or JavaScript.
-   **BREAKING** FileArtifactMutableView is now named FileMutableView

### Fixed

-   @any parameter validation regex now works in both Java and
    Javascript
-   Double-quoted strings in Rug DSL are now interpreted similarly to
    Java double-quoted strings
-   LABEL section in files parsed by Dockerfile type did not handle
    multi-line strings correctly as per [#140][140]

[140]: https://github.com/atomist/rug/issues/140

## [0.7.1] - 2016-12-19

[0.7.1]: https://github.com/atomist/rug/compare/0.7.0...0.7.1

### Added

-   Assertions can use 'not'
-   Improve javadoc

### Changed

-   Various microgrammar improvements
-   Update to latest rug-typescript-compiler

## [0.7.0] - 2016-12-16

[0.7.0]: https://github.com/atomist/rug/compare/0.6.0...0.7.0

### Changed

-   Breaking change to the Typescript Rug programming
    model. Decorators are no longer used, the interface signatures
    have changed, and the mechanism for retrieving the
    PathExpressionEngine has changed. See atomist/rug#24 for details.
-   Breaking change to the Message trait. Now required to implement
    withActionNamed
-   Breaking change to message.Rug interface, type is now part of Rug
    not calledback

### Added

-   Event handlers definitions moved into Rug open source project
-   Added new node levels in microgrammar returns

## [0.6.0] - 2016-12-14

[0.6.0]: https://github.com/atomist/rug/compare/0.5.4...0.6.0

### Changed

-   Improved microgrammer tests
-   Fixed error in MatcherMicrogrammar.strictMatch with extra node
    level
-   Update dependencies to latest
-   Only load javascript from .atomist directory

### Added

-   Added Rename for microgrammers

## [0.5.4] - 2016-12-12

[0.5.4]: https://github.com/atomist/rug/compare/0.5.3...0.5.4

Mutually assured success release

### Changed

-   TypeScript Rugs are loaded first, allowing them to be called from
    Rug DSL Rugs
-   All parameter regular expressions must be anchored, i.e., start
    with `^` and end with `$`
-   The name of the Atomist ignore file is now `.atomist/ignore`
-   project.AddFile is now idempotent

### Added

-   Documentation
-   This CHANGELOG

## [0.5.3] - 2016-12-09

The single triple double release

[0.5.3]: https://github.com/atomist/rug/compare/0.5.2...0.5.3

### Fixed

-   Single double-quotes are now properly ignored within triple
    double-quotes

### Changed

-   Timestamped builds are now published to a dev repo
